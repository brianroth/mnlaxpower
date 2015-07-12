package dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Division {
    @JsonProperty("ID")
    public long id;

    @JsonProperty("Name")
    public String name;

    @JsonProperty("SubDivisions")
    public List<Division> divisions;

    @JsonProperty("Teams")
    public List<Team> teams;
}
