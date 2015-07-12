package dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Season {
    @JsonProperty("ID")
    public long id;

    @JsonProperty("season")
    public String name;

    @JsonProperty("SubDivisions")    
    public List<Division> divisions;
}
