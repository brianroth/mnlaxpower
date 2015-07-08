package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Index
public class Season {
    @Id
    @JsonProperty("ID")
    private long id;

    @JsonProperty("season")
    private String name;

    @JsonProperty("SubDivisions")
    private List<Division> divisions;

    // Required for JSON deserialization
    private Season() {
    }

    public Season(Long id, String name) {
        this();
        this.name = name;
        this.id = id;
        this.divisions = new ArrayList<Division>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }
}
