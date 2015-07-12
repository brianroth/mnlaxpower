package models;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Game {

    @Id
    private long id;

    @Index
    private long homeTeamId;

    @Load
    private Ref<Team> homeTeam;

    private int homeGoals;

    @Index
    private long awayTeamId;

    @Load
    private Ref<Team> awayTeam;

    private int awayGoals;

    private String location;
    
    private String date;

    private Game() {

    }

    public Game(long id) {
        this();
        this.id = id;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Team getHomeTeam() {
        return homeTeam.get();
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = Ref.create(homeTeam);
    }

    public Team getAwayTeam() {
        return awayTeam.get();
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = Ref.create(awayTeam);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
