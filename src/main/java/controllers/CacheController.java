package controllers;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Division;
import models.Game;
import models.Season;
import models.Team;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;
import ninja.session.FlashScope;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.DivisionDao;
import dao.GameDao;
import dao.SeasonDao;
import dao.TeamDao;
import dto.ApiResult;
import dto.Contestant;

@Singleton
@FilterWith(AppEngineFilter.class)
public class CacheController {

    private final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Inject
    private SeasonDao seasonDao;

    @Inject
    private DivisionDao divisionDao;

    @Inject
    private TeamDao teamDao;

    @Inject
    private GameDao gameDao;

    public Result recalculate(FlashScope flashScope) {
        Result result = Results.redirect("/");

        try {

            Season season = seasonDao.findById(12179);

            for (Division division : season.getDivisions()) {
                recalculate(division);
            }

        } catch (Exception e) {
            logger.error("An error has occurred updating cache", e);
            result.render("error", e.getMessage());
        }

        return result;
    }

    public Result recache(@PathParam("d") String divisionId) {
        Result result = Results.redirect("/");

        try {
            if (StringUtils.isNumeric(divisionId)) {

                logger.info("Re-caching division {}", divisionId);

                Division division = divisionDao.findById(Long.parseLong(divisionId));

                if (null != division) {
                    cacheTeamResults(division);
                }
            } else {
                logger.info("Re-caching season {}", 12179);

                Season season = cacheSeason(12179);

                for (Division division : season.getDivisions()) {
                    cacheTeamResults(division);
                }
            }

        } catch (Exception e) {
            logger.error("An error has occurred updating cache", e);
            result.render("error", e.getMessage());
        }

        return result;
    }

    private void cacheTeamResults(Division division) throws JsonParseException, JsonMappingException, IOException {

        Client client = ClientBuilder.newClient();

        ObjectMapper mapper = new ObjectMapper();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/results");

        for (Team team : division.getTeams()) {

            WebTarget webTargetWithQueryParam = webTarget.queryParam("TeamID", team.getId()).queryParam("org",
                    "youthlaxmn.org");

            Invocation.Builder invocationBuilder = webTargetWithQueryParam.request(MediaType.APPLICATION_JSON);

            Response response = invocationBuilder.get();

            String responseAsString = response.readEntity(String.class);

            ApiResult apiResult = mapper.readValue(responseAsString, ApiResult.class);

            int wins = 0;
            int losses = 0;
            int ties = 0;
            int gamesPlayed = 0;

            for (dto.Game game : apiResult.team.games) {
                Contestant home = game.home;
                Contestant away = game.away;

                // Ignore games with no score, it never happened
                if ((home.score + away.score) > 0) {

                    gamesPlayed++;

                    gameDao.findOrCreate(game.id, home.teamId, home.score, away.teamId, away.score, game.date,
                            game.facility.name);

                    if (home.teamId == team.getId()) {
                        if (home.score > away.score) {
                            wins++;
                        } else if (home.score < away.score) {
                            losses++;
                        } else {
                            ties++;
                        }
                    } else {
                        if (away.score > home.score) {
                            wins++;
                        } else if (away.score < home.score) {
                            losses++;
                        } else {
                            ties++;
                        }
                    }

                    team.setGamesPlayed(gamesPlayed);
                    team.setWins(wins);
                    team.setLosses(losses);
                    team.setTies(ties);
                    team.setWp((1.0 * wins) / gamesPlayed);

                    logger.info("Team {} has {} wins, {} losses and {} ties", team.getName(), team.getWins(),
                            team.getLosses(), team.getTies());

                    teamDao.save(team);
                }
            }
        }

        division.setLastRecache(new Date());
        divisionDao.save(division);
    }

    private Season cacheSeason(long seasonId) throws JsonParseException, JsonMappingException, IOException {

        Season season = null;

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions")
                .queryParam("season", 12179).queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        dto.Season[] seasons = mapper.readValue(responseAsString, dto.Season[].class);

        for (dto.Season seasonDto : seasons) {
            season = seasonDao.findOrCreate(12179, seasonDto.name);

            logger.info("Season {} has {} divisions", seasonDto.name, seasonDto.divisions.size());

            for (dto.Division divisionDto : seasonDto.divisions) {
                Division division = divisionDao.findOrCreate(divisionDto.id, season.getId(), divisionDto.name);

                logger.info("Adding division {} to season {}", division.getName(), season.getName());

                season.getDivisions().add(division);

                for (dto.Division subdivision : divisionDto.divisions) {

                    for (dto.Team teamDto : subdivision.teams) {

                        Team team = teamDao.findOrCreate(teamDto.id, division.getId(), teamDto.name);

                        division.getTeams().add(team);
                    }
                }
            }
        }

        return season;
    }

    private void recalculate(Division division) {

        logger.info("Recalculating division {}", division.getName());

        division.setTeams(teamDao.findAllByDivision(division.getId()));

        // Calculate OWP
        for (Team team : division.getTeams()) {
            Set<Team> opponents = findAllOpponents(team);

            // TODO: Throw out games against this team when calculating OWP
            // This means that owp won't be persistable again because it's
            // definition varies from the perspective of the current team.

            int gamesPlayed = 0;
            int wins = 0;

            for (Team opponent : opponents) {
                gamesPlayed += opponent.getGamesPlayed();
                wins += opponent.getWins();
            }

            team.setOwp((1.0 * wins) / gamesPlayed);
        }

        // TODO: Calculate OOWP
        for (Team team : division.getTeams()) {
            Set<Team> opponents = findAllOpponents(team);

            int gamesPlayed = 0;
            int wins = 0;

            for (Team oppenent : opponents) {
                Set<Team> opponentOpponents = findAllOpponents(oppenent);

                for (Team opponentOpponent : opponentOpponents) {
                    gamesPlayed += opponentOpponent.getGamesPlayed();
                    wins += opponentOpponent.getWins();
                }
            }

            team.setOowp((1.0 * wins) / gamesPlayed);
        }

        // Calculate RPI and save team
        for (Team team : division.getTeams()) {
            team.setRpi(0.25 * team.getWp() + 0.5 * team.getOwp() + 0.25 * team.getOowp());

            teamDao.save(team);
        }

        division.setLastRecalculate(new Date());
        divisionDao.save(division);
    }

    private Set<Team> findAllOpponents(Team team) {

        if (null == team.getOpponents()) {
            Set<Team> opponents = new HashSet<Team>();

            for (Game game : gameDao.findByHomeTeamId(team.getId())) {
                opponents.add(teamDao.findById(game.getAwayTeamId()));
            }

            for (Game game : gameDao.findByAwayTeamId(team.getId())) {
                opponents.add(teamDao.findById(game.getHomeTeamId()));
            }

            team.setOpponents(opponents);

            return opponents;
        } else {
            return team.getOpponents();
        }
    }
}
