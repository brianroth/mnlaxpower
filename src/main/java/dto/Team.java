package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
    
    @JsonProperty("ID")
    public long id;
    
    @JsonProperty("Name")
    public String name;

    @JsonProperty("teamName")
    public String teamName;
    
    @JsonProperty("games")
    public Game[] games;
}
