package test.project.leonparser.service.leon;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import test.project.leonparser.exception.ApiCallException;
import test.project.leonparser.service.DataFetchService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class LeonDataFetchService implements DataFetchService {
    @Value("${leon.urlTemplate.get.sportsLeagues}")
    private String urlGetAllSportsLeagues;
    @Value("${leon.urlTemplate.get.matchByLeagueId}")
    private String urlGetMatchesByLeagueId;
    @Value("${leon.urlTemplate.get.marketByMatchId}")
    private String urlGetMarketsByMatchId;

    private final ExecutorService executorService;

    @Override
    public String getAllSportsLeagues() {
        return fetchData(urlGetAllSportsLeagues);
    }

    @Override
    public CompletableFuture<String> getMatchesByLeagueId(Long leagueId) {
        String url = urlGetMatchesByLeagueId.formatted(leagueId);
        return fetchDataAsync(url);
    }

    @Override
    public CompletableFuture<String> getMarketsByMatchId(Long matchId) {
        String url = urlGetMarketsByMatchId.formatted(matchId);
        return fetchDataAsync(url);
    }

    private CompletableFuture<String> fetchDataAsync(String url) {
        return CompletableFuture.supplyAsync(() -> fetchData(url), executorService);
    }

    private String fetchData(String url) {
        try {
            String body = Jsoup.connect(url).ignoreContentType(true).execute().body();
            if (body.isBlank()) {
                throw new ApiCallException("API response returned empty data");
            }
            return body;
        } catch (Exception e) {
            throw new ApiCallException(e);
        }
    }
}
