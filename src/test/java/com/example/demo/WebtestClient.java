package connect.connectserver.units;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WebtestClient {

    @Test
    public void test() {

        WebTestClient h = WebTestClient.bindToController(new TestController()).build();
        WebTestClient.ResponseSpec h9 = h.get().uri(uriBuilder -> uriBuilder
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

    @RestController
    public class TestController {


        String basePath = "";


        @GetMapping(path = "/test")
        public Flux<DataBuffer> test(@RequestParam("fileName") String fileName) {
            Path p = Paths.get(new File(basePath + fileName).getAbsolutePath());
            DataBufferFactory dbf = new DefaultDataBufferFactory();
            Flux<DataBuffer> flux = DataBufferUtils.read(p, dbf, 256 * 256);
            return flux;
        }


    }

}
