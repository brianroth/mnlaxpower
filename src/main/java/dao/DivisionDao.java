package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Division;
import models.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class DivisionDao extends BusinessObjectDao<Division> {

    private final Logger logger = LoggerFactory.getLogger(DivisionDao.class);

    @Inject
    private TeamDao teamDao;

    public List<Division> findAllBySeason(long seasonId) {
        List<Division> divisions = objectify.get().load().type(Division.class).filter("seasonId", seasonId).list();

        Collections.sort(divisions);

        return divisions;
    }

    public Division findById(long id) {
        Division division = objectify.get().load().type(Division.class).filter("id", id).first().now();

        if (null != division) {
            division.setTeams(teamDao.findAllByDivision(division.getId()));
        }

        return division;
    }

    public Division findOrCreate(long id, long seasonId, String name) {
        Division division = objectify.get().load().type(Division.class).filter("id", id).first().now();

        if (null == division) {
            logger.info("Division was null after load");

            division = new Division(id, seasonId, name);

            logger.info("Creating division {}({}) to season {}", division.getName(), division.getId(),
                    division.getSeasonId());

            objectify.get().save().entity(division);
        } else {

            logger.info("Division {}({}) belonging to season {} was not null after load: id {} seasonId {}",
                    division.getName(), division.getId(), division.getSeasonId(), id, seasonId);

            division.setTeams(new ArrayList<Team>());
        }

        return division;
    }
}