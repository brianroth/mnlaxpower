package models;

import java.util.Set;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Team implements Comparable<Team> {
    @Id
    private long id;

    private String name;

    private int gamesPlayed;

    private int wins;

    @Ignore
    private int losses;

    @Ignore
    private int ties;

    private double wp;

    private double owp;

    private double oowp;

    private double rpi;

    @Load
    private Ref<Division> division;

    @Index
    private long divisionId;

    @Ignore
    private transient Set<Team> opponents;

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
        this.wp = (double) (Math.round(wp * 100)) / 100.0;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    @Override
    public int compareTo(Team otherTeam) {
        if (otherTeam.getRpi() > this.getRpi()) {
            return 1;
        } else if (otherTeam.getRpi() < this.getRpi()) {
            return -1;
        } else {
            return 0;
        }

    }

    public double getOwp() {
        return owp;
    }

    public int getPoints() {
       return 3 * getWins(); 
    }
    
    public void setOwp(double owp) {
        this.owp = (double) (Math.round(owp * 100)) / 100.0;
    }

    public double getRpi() {
        return rpi;
    }

    public void setRpi(double rpi) {
        this.rpi = (double) (Math.round(rpi * 100)) / 100.0;
    }

    public double getOowp() {
        return oowp;
    }

    public void setOowp(double oowp) {
        this.oowp = oowp;
    }

    public Set<Team> getOpponents() {
        return opponents;
    }

    public void setOpponents(Set<Team> opponents) {
        this.opponents = opponents;
    }

    public Division getDivision() {
        return division.get();
    }

    public void setDivision(Division division) {
        this.division = Ref.create(division);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (divisionId ^ (divisionId >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        if (divisionId != other.divisionId)
            return false;
        if (id != other.id)
            return false;
        return true;
    }
}