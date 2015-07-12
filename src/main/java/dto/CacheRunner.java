package dto;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheRunner {

    public static final Logger logger = LoggerFactory.getLogger(CacheRunner.class);

    public void getTeams() throws JsonParseException, JsonMappingException, IOException {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions")
                .queryParam("season", 12179).queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        logger.info("response is \n{}", responseAsString);
        dto.Season[] seasons = mapper.readValue(responseAsString, dto.Season[].class);

        for (dto.Season seasonDto : seasons) {

            logger.info("Season {} has {} divisions", seasonDto.name, seasonDto.divisions.size());

            for (dto.Division divisionDto : seasonDto.divisions) {

                logger.info("Division has {} subdivisions", divisionDto.divisions);

                for (dto.Division subDivision : divisionDto.divisions) {

                    logger.info("Division {} has {} subdivisions", subDivision.name, subDivision.divisions);

                    for (dto.Team teamDto : subDivision.teams) {
                        logger.info("Team {}", teamDto.name);
                    }
                }
            }
        }
    }

    public void getResults(long teamId) throws JsonParseException, JsonMappingException, IOException {

        Client client = ClientBuilder.newClient();

        ObjectMapper mapper = new ObjectMapper();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/results").queryParam("TeamID", teamId)
                .queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        logger.info(responseAsString);

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

                logger.info("Game home {} away {} date {} location {}", game.home.teamId, game.away.teamId, game.date, game.facility.name);

                if (home.teamId == teamId) {
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

                logger.info("Team {} has {} wins, {} losses and {} ties", teamId, wins, losses, ties);
            }
        }

    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        CacheRunner runner = new CacheRunner();

        runner.getResults(376990);
    }

}
