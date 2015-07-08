package dao;

import java.util.ArrayList;
import java.util.List;

import models.Division;
import models.Season;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public class SeasonDao {

    private final Logger logger = LoggerFactory.getLogger(SeasonDao.class);

    @Inject
    private DivisionDao divisionDao;

    @Inject
    private Provider<Objectify> objectify;

    public List<Season> findAll() {
        return objectify.get().load().type(Season.class).list();
    }

    public Season findById(long id) {
        Season season = objectify.get().load().type(Season.class).filter("id", id).first().now();

        season.setDivisions(divisionDao.findAllBySeason(season.getId()));
        
        logger.info("Season was {} {}", season.getId(), season.getName());
        
        return season;
    }

    public Season findOrCreate(long id, String name) {
        Season season = objectify.get().load().type(Season.class).filter("id", id).first().now();

        if (null == season) {
            season = new Season(id, name);
        } else {
            season.setName(name);
            season.setDivisions(new ArrayList<Division>());
        }

        objectify.get().save().entity(season);
        
        return season;
    }
}