package test.project.leonparser.service.leon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.project.leonparser.exception.ProcessingJsonException;
import test.project.leonparser.model.dto.MatchDataDto;
import test.project.leonparser.model.dto.parserservice.*;
import test.project.leonparser.service.DataFetchService;
import test.project.leonparser.service.ParserService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LeonParserService implements ParserService {

    private static final String JSON_PATH_EVENTS = "events";

    private final ObjectMapper objectMapper;
    private final DataFetchService fetcherService;

    @Override
    public List<Long> getAllSportsTopLeagueIds() {
        try {
            String allSportsLeagues = fetcherService.getAllSportsLeagues();
            List<SportDto> sportDtos = objectMapper.readValue(allSportsLeagues, new TypeReference<>() {});

            return sportDtos.stream()
                    .map(SportDto::getRegions)
                    .flatMap(regions -> regions.stream().map(RegionDto::getLeagues))
                    .flatMap(leagues -> leagues.stream().filter(LeagueDto::isTopLeague))
                    .map(LeagueDto::getId)
                    .collect(toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<List<MatchDto>> getMatchesByLeagueId(Long leagueId, Integer amountOfMatches) {
        try {
            return fetcherService.getMatchesByLeagueId(leagueId)
                    .thenApply(data -> readJsonTree(data, JSON_PATH_EVENTS))
                    .thenApply(
                            matches -> matches.valueStream()
                                    .map(this::parseMatch)
                                    .limit(amountOfMatches)
                                    .collect(toList())
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<MatchDataDto> getMarketsByMatchId(Long matchId) {
        return fetcherService
                .getMarketsByMatchId(matchId)
                .thenApply(this::readValueToPrintMatchDataDto);
    }

    private MatchDataDto readValueToPrintMatchDataDto(String marketsByMatchId) {
        try {
            return objectMapper.readValue(marketsByMatchId, MatchDataDto.class);
        } catch (Exception e) {
            throw new ProcessingJsonException("Could not parse Json string to MatchDataDto class", e);
        }
    }

    private JsonNode readJsonTree(String data, String pathToRead) {
        try {
            return objectMapper.readTree(data).path(pathToRead);
        } catch (Exception e) {
            throw new ProcessingJsonException("Could not retrieve Json string to JsonNode", e);
        }
    }

    private MatchDto parseMatch(JsonNode matches) {
        try {
            return objectMapper.treeToValue(matches, MatchDto.class);
        } catch (JsonProcessingException e) {
            throw new ProcessingJsonException("Could not parse JsonNode to MatchDto class", e);
        }
    }
}
