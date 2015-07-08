package models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class Game {

    @Id
    private long id;

    private long homeTeamId;

    private int homeGoals;

    private long awayTeamId;

    private int awayGoals;

    // Required for JSON deserialization
    private Game() {
    }
    
    public Game(long id, long homeTeamId, int homeGoals, long awayTeamId, int awayGoals) {
        this();
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.homeGoals = homeGoals;
        this.awayTeamId = awayTeamId;
        this.awayGoals = awayGoals;
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
}
