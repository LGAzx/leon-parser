package test.project.leonparser.service;

import java.util.concurrent.CompletableFuture;

public interface DataFetchService {

    String getAllSportsLeagues();

    CompletableFuture<String> getMatchesByLeagueId(Long eventId);

    CompletableFuture<String> getMarketsByMatchId(Long matchId);
}
