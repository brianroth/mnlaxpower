package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Game;
import models.Team;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.GameDao;
import dao.TeamDao;
import filters.OptionsFilter;

@Singleton
@FilterWith({ AppEngineFilter.class, OptionsFilter.class })
public class TeamsController {
    private final Logger logger = LoggerFactory.getLogger(TeamsController.class);

    @Inject
    private TeamDao teamDao;

    @Inject
    private GameDao gameDao;

    public Result show(Context context, @PathParam("teamId") String teamId) {

        logger.info("param teamId = {}", teamId);

        Team team = teamDao.findById(Long.parseLong(teamId));

        List<Game> games = new ArrayList<Game>();

        games.addAll(gameDao.findByHomeTeamId(team.getId()));
        games.addAll(gameDao.findByAwayTeamId(team.getId()));

        Result result = Results.html();

        result.render("team", team);
        result.render("games", games);
        result.render("selectedDivision", team.getDivision().getId());

        result.render("selectedSeason", context.getAttribute(OptionsFilter.SEASON_ID));
        result.render("season", context.getAttribute(OptionsFilter.SEASON));
        result.render("division", team.getDivision());
        result.render("seasons", context.getAttribute(OptionsFilter.SEASONS));
        result.render("divisions", context.getAttribute(OptionsFilter.DIVISIONS));

        return result;
    }

    public TeamDao getTeamDao() {
        return teamDao;
    }

    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }

    public GameDao getGameDao() {
        return gameDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
