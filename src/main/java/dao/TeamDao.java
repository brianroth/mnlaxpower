package dao;

import java.util.Collections;
import java.util.List;

import models.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class TeamDao extends BusinessObjectDao<Team> {

    private final Logger logger = LoggerFactory.getLogger(TeamDao.class);

    @Inject
    private DivisionDao divisionDao;

    public List<Team> findAllByDivision(long divisionId) {
        List<Team> teams = objectify.get().load().type(Team.class).filter("divisionId", divisionId).list();

        Collections.sort(teams);

        return teams;
    }

    public Team findOrCreate(long id, long divisionId, String name) {
        Team team = findById(id);

        if (null == team) {
            logger.info("Team was null after load");

            team = new Team(id, divisionId, name);
        }

        team.setDivision(divisionDao.findById(divisionId));

        save(team);

        return team;
    }
}
