package test.project.leonparser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {
    private static final int AMOUNT_OF_THREADS = 3;

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(AMOUNT_OF_THREADS);
    }
}
