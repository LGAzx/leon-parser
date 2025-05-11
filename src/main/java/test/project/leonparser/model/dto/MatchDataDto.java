package test.project.leonparser.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import test.project.leonparser.model.dto.parserservice.MarketDto;
import test.project.leonparser.util.UnixTimeConverter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDataDto {
    private Long id;
    private String name;
    @JsonDeserialize(using = UnixTimeConverter.class)
    private LocalDateTime kickoff;
    private LeagueDataDto league;
    private List<MarketDto> markets;
}
