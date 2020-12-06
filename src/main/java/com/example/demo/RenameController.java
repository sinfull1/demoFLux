package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@Component
public class RenameController {


    private static final Logger logger = LoggerFactory.getLogger(RenameController.class);

    String basePath = "CHANGE_TO_PATH";


    private final FileEventProcessor fileEventProcessor;

    @Autowired
    public RenameController(FileEventProcessor fileEventProcessor) {
        this.fileEventProcessor=fileEventProcessor;
    }

    @GetMapping(path = "/test")
    public Flux<DataBuffer> test(@RequestParam ("fileName") String fileName)  {
        Path p = Paths.get(new File(basePath+fileName).getAbsolutePath());
        DataBufferFactory dbf = new DefaultDataBufferFactory();
        Flux<DataBuffer> flux= DataBufferUtils.read(p, dbf, 256*256);
        return flux;
    }

    @GetMapping(path = "/reader", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Object> reader()  {
        FluxProcessor<Object,Object> s = fileEventProcessor.getProcessor( UUID.randomUUID().toString());
        return s.doOnCancel(()->{
            fileEventProcessor.cleanProcessor(s);});
    }

}