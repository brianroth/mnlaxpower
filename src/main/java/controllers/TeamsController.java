package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Game;
import models.Team;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.DivisionDao;
import dao.GameDao;
import dao.TeamDao;

@Singleton
@FilterWith(AppEngineFilter.class)
public class TeamsController {
    private final Logger logger = LoggerFactory.getLogger(TeamsController.class);

    @Inject
    private TeamDao teamDao;

    @Inject
    private GameDao gameDao;

    @Inject DivisionDao divisionDao;
    
    public Result show(@PathParam("teamId") String teamId) {

        logger.info("param teamId = {}", teamId);

        Team team = teamDao.findById(Long.parseLong(teamId));

        List<Game> games = new ArrayList<Game>();

        games.addAll(gameDao.findByHomeTeamId(team.getId()));
        games.addAll(gameDao.findByAwayTeamId(team.getId()));

        Result result = Results.html();

        result.render("team", team);
        result.render("games", games);
        result.render("selectedDivision", team.getDivisionId());

        return result;

    }

}