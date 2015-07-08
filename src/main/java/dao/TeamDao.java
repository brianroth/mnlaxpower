package dao;

import java.util.Collections;
import java.util.List;

import models.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public class TeamDao {
    private final Logger logger = LoggerFactory.getLogger(TeamDao.class);

    @Inject
    private Provider<Objectify> objectify;

    public List<Team> findAllByDivision(long divisionId) {
        List<Team> teams =objectify.get().load().type(Team.class).filter("divisionId", divisionId).list();

        Collections.sort(teams);
        
        return teams;
    }

    public Team findOrCreate(Long id, long divisionId, String name) {
        Team team = objectify.get().load().type(Team.class).filter("id", id).first().now();

        if (null == team) {
            logger.info("Team was null after load");

            team = new Team(id, divisionId, name);
            objectify.get().save().entity(team);
        }

        return team;
    }
    
    public Team save(Team team) {
        objectify.get().save().entity(team);
        
        return team;
    }
}
