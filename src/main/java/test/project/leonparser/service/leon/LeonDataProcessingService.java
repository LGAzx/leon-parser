package test.project.leonparser.service.leon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.leonparser.mapper.MatchDataMapper;
import test.project.leonparser.model.dto.parserservice.MatchDto;
import test.project.leonparser.service.DataProcessingService;
import test.project.leonparser.service.ParserService;
import test.project.leonparser.service.PrintService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LeonDataProcessingService implements DataProcessingService {

    private static final Integer AMOUNT_OF_MATCHES = 2;

    private final ParserService parserService;
    private final PrintService printService;
    private final MatchDataMapper matchDataMapper;
    private final ExecutorService executorService;

    @Override
    public void printTopLeaguesMatchesMarketsDetails() {
        List<Long> allSportsTopLeagueIds = parserService.getAllSportsTopLeagueIds();

        List<CompletableFuture<Void>> futures = allSportsTopLeagueIds.stream()
                .map(leagueId ->
                        parserService.getMatchesByLeagueId(leagueId, AMOUNT_OF_MATCHES)
                                .thenApplyAsync(matches -> matches.stream()
                                        .map(MatchDto::getId)
                                        .map(parserService::getMarketsByMatchId)
                                        .collect(toList()), executorService)
                                .thenAcceptAsync(matchData -> matchData.stream()
                                        .map(CompletableFuture::join)
                                        .map(matchDataMapper::map)
                                        .forEach(printService::print), executorService)
                ).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
