package  com.example.demo;


import com.example.demo.FileEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition", "Content-Type"})
@RestController
@Component
public class ReaderController {


    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    String basePath = "C:\\Users\\micro\\IdeaProjects\\react-spring-boot\\connect-server\\src\\main\\resources\\";


    private final FileEventProcessor fileEventProcessor;

    @Autowired
    public ReaderController(FileEventProcessor fileEventProcessor) {
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
        return  fileEventProcessor.getFlux();

    }

}