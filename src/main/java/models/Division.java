package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Division implements Comparable<Division> {
    @Id
    private long id;

    @Index
    private boolean base;
    
    private String name;

    @Ignore
    private List<Team> teams;

    private Date lastRecalculate;

    private Date lastRecache;
    
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

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public Date getLastRecalculate() {
        return lastRecalculate;
    }

    public void setLastRecalculate(Date lastRecalculate) {
        this.lastRecalculate = lastRecalculate;
    }

    public Date getLastRecache() {
        return lastRecache;
    }

    public void setLastRecache(Date lastRecache) {
        this.lastRecache = lastRecache;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + (int) (seasonId ^ (seasonId >>> 32));
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
        Division other = (Division) obj;
        if (id != other.id)
            return false;
        if (seasonId != other.seasonId)
            return false;
        return true;
    }
}