package controllers;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Division;
import models.Metrics;
import models.Season;
import models.Team;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.DivisionDao;
import dao.GameDao;
import dao.MetricsDao;
import dao.SeasonDao;
import dao.TeamDao;

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

    @Inject
    private MetricsDao metricsDao;

    public Result recalculate() {
        long startTime = System.currentTimeMillis();

        Result result = Results.html();

        try {

            Metrics metrics = metricsDao.find();

            metrics.setLastRecalculate(new Date());

            metricsDao.save(metrics);

        } catch (Exception e) {
            logger.error("An error has occurred updating cache", e);
            result.render("error", e.getMessage());
        }

        result.render("time", System.currentTimeMillis() - startTime);

        return result;
    }

    public Result recache(@PathParam("d") String divisionId) {
        long startTime = System.currentTimeMillis();

        Result result = Results.html();

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

            Metrics metrics = metricsDao.find();

            metrics.setLastRecache(new Date());

            metricsDao.save(metrics);

        } catch (Exception e) {
            logger.error("An error has occurred updating cache", e);
            result.render("error", e.getMessage());
        }

        result.render("time", System.currentTimeMillis() - startTime);

        return result;
    }

    private void cacheTeamResults(Division division) throws JsonParseException, JsonMappingException, IOException {

        Client client = ClientBuilder.newClient();

        ObjectMapper mapper = new ObjectMapper();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/results");

        for (Team team : division.getTeams()) {

            WebTarget helloworldWebTargetWithQueryParam = webTarget.queryParam("TeamID", team.getId()).queryParam(
                    "org", "youthlaxmn.org");

            Invocation.Builder invocationBuilder = helloworldWebTargetWithQueryParam
                    .request(MediaType.APPLICATION_JSON);

            Response response = invocationBuilder.get();

            String responseAsString = response.readEntity(String.class);

            ApiResult apiResult = mapper.readValue(responseAsString, ApiResult.class);

            int wins = 0;
            int losses = 0;
            int ties = 0;
            int gamesPlayed = 0;

            for (GameResult gameResult : apiResult.teamResult.games) {
                ContestantResult home = gameResult.home;
                ContestantResult away = gameResult.away;

                gamesPlayed++;

                gameDao.findOrCreate(gameResult.id, home.teamId, home.score, away.teamId, away.score);

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
            }

            team.setGamesPlayed(gamesPlayed);
            team.setWins(wins);
            team.setLosses(losses);
            team.setTies(ties);
            team.setWp((1.0 * wins) / gamesPlayed);

            logger.info("Team {} has {} wins, {} losses and {} ties", team.getName(), team.getWins(), team.getLosses(),
                    team.getTies());

            teamDao.save(team);
        }
    }

    private Season cacheSeason(long seasonId) throws JsonParseException, JsonMappingException, IOException {

        Season season = null;

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions");

        WebTarget helloworldWebTargetWithQueryParam = webTarget.queryParam("season", 12179).queryParam("org",
                "youthlaxmn.org");

        Invocation.Builder invocationBuilder = helloworldWebTargetWithQueryParam.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        Season[] seasons = mapper.readValue(responseAsString, Season[].class);

        for (Season seasonDto : seasons) {
            season = seasonDao.findOrCreate(12179, seasonDto.getName());

            logger.info("Season {} has {} divisions", seasonDto.getName(), seasonDto.getDivisions().size());

            for (Division divisionDto : seasonDto.getDivisions()) {
                Division division = divisionDao
                        .findOrCreate(divisionDto.getId(), season.getId(), divisionDto.getName());

                logger.info("Adding division {} to season {}", division.getName(), season.getName());

                season.getDivisions().add(division);

                for (Division subdivision : divisionDto.getDivisions()) {

                    for (Team teamDto : subdivision.getTeams()) {

                        Team team = teamDao.findOrCreate(teamDto.getId(), division.getId(), teamDto.getName());

                        division.getTeams().add(team);
                    }
                }
            }
        }

        return season;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiResult {
        @JsonProperty("results")
        TeamResult teamResult;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamResult {
        @JsonProperty("teamName")
        String name;

        @JsonProperty("games")
        GameResult[] games;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameResult {

        @JsonProperty("id")
        long id;

        @JsonProperty("home")
        ContestantResult home;

        @JsonProperty("away")
        ContestantResult away;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContestantResult {
        @JsonProperty("id")
        int teamId;

        @JsonProperty("score")
        int score;
    }
}
