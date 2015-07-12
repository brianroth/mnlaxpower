package dao;

import java.util.List;

import models.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public class GameDao {
    private final Logger logger = LoggerFactory.getLogger(GameDao.class);

    @Inject
    private TeamDao teamDao;

    @Inject
    private Provider<Objectify> objectify;

    public Game findById(long id) {
        Game game = objectify.get().load().type(Game.class).filter("id", id).first().now();

        if (null != game) {
            logger.info("Game was {} {} {}", game.getId(), game.getHomeTeamId(), game.getAwayTeamId());
        } else {
            logger.info("Game {} was not found", id);
        }

        return game;
    }

    public List<Game> findByHomeTeamId(long teamId) {
        return objectify.get().load().type(Game.class).filter("homeTeamId", teamId).list();
    }

    public List<Game> findByAwayTeamId(long teamId) {
        return objectify.get().load().type(Game.class).filter("awayTeamId", teamId).list();
    }

    public Game findOrCreate(long id, long homeTeamId, int homeGoals, long awayTeamId, int awayGoals, String date,
            String location) {
        Game game = objectify.get().load().type(Game.class).filter("id", id).first().now();

        if (null == game) {
            logger.info("Game was null after load");
            game = new Game(id);
        }

        game.setHomeTeamId(homeTeamId);
        game.setHomeGoals(homeGoals);
        game.setAwayTeamId(awayTeamId);
        game.setAwayGoals(awayGoals);
        game.setDate(date);
        game.setLocation(location);
        game.setAwayTeam(teamDao.findById(awayTeamId));
        game.setHomeTeam(teamDao.findById(homeTeamId));

        objectify.get().save().entity(game);

        return game;
    }
}
