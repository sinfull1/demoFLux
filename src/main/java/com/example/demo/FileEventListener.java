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
        checkFileGrowth(files);
        DataBufferFactory dbf = new DefaultDataBufferFactory();
        Flux<DataBuffer> buffer = DataBufferUtils.read(p, dbf, 256*64);
        Flux<String> stringFlux = buffer.flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            return Mono.just(new String(bytes, StandardCharsets.UTF_8));
        });
        stringFlux.subscribe(k->eventProcessor.getSink().emitNext(k, Sinks.EmitFailureHandler.FAIL_FAST));
        stringFlux.blockLast();
    }

    private void checkFileGrowth(File p)  {


            while ( !p.renameTo(p)) {
                try {
                    System.out.println("waiting");
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

    }

}
