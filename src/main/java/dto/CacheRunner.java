package dto;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CacheRunner {

    public static final Logger logger = LoggerFactory.getLogger(CacheRunner.class);

    public Season[] getSeasons() throws JsonParseException, JsonMappingException, IOException {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/seasons").queryParam("org",
                "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        dto.Season[] seasons = mapper.readValue(responseAsString, dto.Season[].class);

        return seasons;
    }

    public Division[] getDivisions(long seasonId) throws JsonParseException, JsonMappingException, IOException {

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions")
                .queryParam("season", seasonId).queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        dto.Season[] seasons = mapper.readValue(responseAsString, dto.Season[].class);

        for (Season season : seasons) {

            logger.info("Season {} has {} divisions", season.name, season.divisions.size());

            for (Division division : season.divisions) {

                logger.info("Season {}({}) Division {}({})", season.name, seasonId, division.name, division.id);
                
//                for (Team team : division.teams) {
//                    logger.info("Season {} ({}) division {}({}) team {} ", season.name, seasonId, division.name,
//                            division.id, team.name);
//                }

                for (Division subdivision : division.divisions) {

                    logger.info("Season {}({}) Division {}({})", season.name, seasonId, subdivision.name, subdivision.id);
                    
                    for (Team team : subdivision.teams) {
//                        logger.info("Season {} ({}) division {}({}) team {} ", season.name, seasonId, division.name,
//                                division.id, team.name);
                    }
                }
            }
        }

        return null;
    }

    public void getTeams() throws JsonParseException, JsonMappingException, IOException {

        for (Season season : getSeasons()) {

            Client client = ClientBuilder.newClient();

            WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions")
                    .queryParam("season", season.id).queryParam("org", "youthlaxmn.org");

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

            Response response = invocationBuilder.get();

            String responseAsString = response.readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();

            dto.Season[] seasons = mapper.readValue(responseAsString, dto.Season[].class);

            for (dto.Season seasonDto : seasons) {

                logger.info("Season {} has {} divisions", seasonDto.name, seasonDto.divisions.size());

                for (dto.Division divisionDto : seasonDto.divisions) {

                    logger.info("{} division {}", seasonDto.name, divisionDto.name);

                    for (dto.Division subDivision : divisionDto.divisions) {

                        for (dto.Team teamDto : subDivision.teams) {
                            logger.info("{} team {}", seasonDto.name, teamDto.name);
                        }
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

        for (dto.Game game : apiResult.team.games) {
            Contestant home = game.home;
            Contestant away = game.away;

            // Ignore games with no score, it never happened
            if ((home.score + away.score) > 0) {

                logger.info("Game home {} away {} date {} location {}", game.home.teamId, game.away.teamId, game.date,
                        game.facility.name);

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

        Season[] seasons = runner.getSeasons();

        for (Season season : seasons) {
            runner.getDivisions(season.id);
        }
        
        
        
    }
}
