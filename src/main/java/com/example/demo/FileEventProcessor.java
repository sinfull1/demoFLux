package com.example.demo;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import reactor.core.publisher.*;


@Component
public class FileEventProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    Sinks.Many<Object> replaySink = Sinks.many().replay().limit(1);

    public FileEventProcessor() {
    }
    public Flux<Object> getFlux() {
        return replaySink.asFlux();
    }
    public Sinks.Many<Object> getSink() {
        return replaySink;
    }
}



