package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    @JsonProperty("id")
    public long id;

    @JsonProperty("home")
    public Contestant home;

    @JsonProperty("away")
    public Contestant away;
    
    @JsonProperty("date")
    public String date;
    
    public Facility facility;
}