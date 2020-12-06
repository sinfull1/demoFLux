package com.example.demo;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebtestClient {




    @Test
    public void test() throws  JSONException {

        WebTestClient h = WebTestClient.bindToController(new UploadController(new FileEventProcessor())).build();
        WebTestClient.ResponseSpec h9= h.get().uri(uriBuilder -> uriBuilder
                .path("/test")
                .queryParam("fileName", "generated.json")
                .build()).exchange();
        Flux<DataBuffer> g = h9.returnResult(DataBuffer.class).getResponseBody();

        Flux<String> jj = g.flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);
                String jkh = new String(bytes, StandardCharsets.UTF_8);
                return Mono.just(jkh);
          });

        jj.blockLast();


   }



}
