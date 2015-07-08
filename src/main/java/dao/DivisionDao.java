package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Division;
import models.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public class DivisionDao {

    private final Logger logger = LoggerFactory.getLogger(SeasonDao.class);

    @Inject
    private Provider<Objectify> objectify;

    @Inject
    private TeamDao teamDao;

    public List<Division> findAllBySeason(long seasonId) {
        List<Division> divisions = objectify.get().load().type(Division.class).filter("seasonId", seasonId).list();
        
        Collections.sort(divisions);
        
        return divisions;
    }

    public Division findById(long id) {
        Division division = objectify.get().load().type(Division.class).filter("id", id).first().now();

        division.setTeams(teamDao.findAllByDivision(division.getId()));

        logger.info("Division was {} {}", division.getId(), division.getName());

        return division;
    }

    public Division findOrCreate(long id, long seasonId, String name) {
        Division division = objectify.get().load().type(Division.class).filter("id", id).first().now();

        if (null == division) {
            logger.info("Division was null after load");

            division = new Division(id, seasonId, name);
            objectify.get().save().entity(division);
        } else {
            logger.info("Division was not null after load");
            division.setTeams(new ArrayList<Team>());
        }

        return division;
    }
}