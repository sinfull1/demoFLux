package com.example.demo;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import reactor.core.publisher.*;


@Component
public class FileEventProcessor {

    Sinks.Many replaySink = Sinks.many().multicast().onBackpressureBuffer();

    public FileEventProcessor() {
    }
    public Flux<Object> getFlux() {
        return replaySink.asFlux();
    }
    public Sinks.Many<Object> getSink() {
        return replaySink;
    }
}



