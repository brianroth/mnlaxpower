package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contestant {
    @JsonProperty("id")
    public int teamId;

    @JsonProperty("score")
    public int score;
}