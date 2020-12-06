package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class FileEventPublisher {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    public void publishFileEvent(String filePath) {
        FileEvent fileEvent = new FileEvent(filePath);
        applicationEventPublisher.publishEvent(fileEvent);
    }
}
