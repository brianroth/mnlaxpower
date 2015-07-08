package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Index
public class Team implements Comparable<Team> {
    @Id
    @JsonProperty("ID")
    private long id;

    @JsonProperty("Name")
    private String name;

    private int gamesPlayed;

    private int wins;

    private int losses;

    private int ties;

    private double wp;

    private long divisionId;

    // Required for JSON deserialization
    private Team() {
    }

    public Team(Long id, Long divisionId, String name) {
        this();
        this.name = name;
        this.divisionId = divisionId;
        this.id = id;
    }

    public long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(long divisionId) {
        this.divisionId = divisionId;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public double getWp() {
        return wp;
    }

    public void setWp(double wp) {
        this.wp = (double)(Math.round(wp * 100)) / 100.0;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    @Override
    public int compareTo(Team otherTeam) {
        if (otherTeam.getWp() > this.getWp()) {
            return 1;
        } else if (otherTeam.getWp() < this.getWp()) {
            return -1;
        } else {
            return 0;
        }

    }
}