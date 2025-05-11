package test.project.leonparser.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import test.project.leonparser.service.DataProcessingService;

import java.util.concurrent.ExecutorService;

@Component
@RequiredArgsConstructor
public class MatchMarketsDataRunner implements CommandLineRunner {

    private final DataProcessingService dataProcessingService;
    private final ExecutorService executorService;

    @Override
    public void run(String... args) {
        dataProcessingService.printTopLeaguesMatchesMarketsDetails();
        executorService.shutdown();
        System.exit(0);
    }
}
