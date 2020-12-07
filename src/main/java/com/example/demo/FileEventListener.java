package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class FileEventListener implements ApplicationListener<FileEvent> {


    @Autowired
    FileEventProcessor eventProcessor;


    @Override
    public void onApplicationEvent(FileEvent fileEvent) {
        File files = new File(fileEvent.getSource().toString());
        Path p = Paths.get(files.getAbsolutePath());
        AtomicLong d = new AtomicLong();
        try {
            checkFileGrowth(files);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataBufferFactory dbf = new DefaultDataBufferFactory();

        Flux<DataBuffer> buffer = DataBufferUtils.read(p, dbf, 256*64);
        Flux<String> stringFlux = buffer.flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            return Mono.just(new String(bytes, StandardCharsets.UTF_8)+d.incrementAndGet());
        });
        stringFlux.subscribe(k->eventProcessor.getSink().emitNext(k, Sinks.EmitFailureHandler.FAIL_FAST));
        stringFlux.blockLast();
    }

    private void checkFileGrowth(File p) throws InterruptedException {
        long len1 =1;
        long len2 =2;
        while(len1!=len2) {
            len1 = p.length();
            Thread.sleep(100);
            len2 = p.length();
            System.out.println(len1);
            System.out.println(len2);
        }
    }

}
