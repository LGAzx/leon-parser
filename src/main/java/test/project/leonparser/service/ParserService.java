package test.project.leonparser.service;

import test.project.leonparser.model.dto.parserservice.MatchDto;
import test.project.leonparser.model.dto.MatchDataDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ParserService {
    List<Long> getAllSportsTopLeagueIds();
    CompletableFuture<List<MatchDto>> getMatchesByLeagueId(Long leaguesId, Integer amountOfMatches);
    CompletableFuture<MatchDataDto> getMarketsByMatchId(Long matchId);
}
