package models;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Metrics {
    @Id
    private long id;
    
    private Date lastRecalculate;

    private Date lastRecache;
    
    public Metrics() {
        this.id = 1;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
