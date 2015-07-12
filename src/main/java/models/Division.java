package models;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Division implements Comparable<Division> {
    @Id
    private long id;

    private String name;

    private List<Division> divisions;

    private List<Team> teams;

    @Index
    private long seasonId;

    private Division() {
    }

    public Division(long id, long seasonId, String name) {
        this();
        this.id = id;
        this.seasonId = seasonId;
        this.name = name;
        this.teams = new ArrayList<Team>();
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

    public long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(long seasonId) {
        this.seasonId = seasonId;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public int compareTo(Division otherDivision) {
        return getName().compareTo(otherDivision.getName());
    }
}