package service;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ApiResult;
import dto.Game;
import dto.Season;

public class JerseyServiceImpl implements JerseyService {

    public Season[] readSeason(long seasonId) throws JsonParseException, JsonMappingException, IOException {

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/divisions")
                .queryParam("season", seasonId).queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(responseAsString, dto.Season[].class);
    }

    @Override
    public Game[] readGames(long teamId) throws JsonParseException, JsonMappingException, IOException {
        Client client = ClientBuilder.newClient();

        ObjectMapper mapper = new ObjectMapper();

        WebTarget webTarget = client.target("https://api.leagueathletics.com/api/results");

        WebTarget webTargetWithQueryParam = webTarget.queryParam("TeamID", teamId).queryParam("org", "youthlaxmn.org");

        Invocation.Builder invocationBuilder = webTargetWithQueryParam.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        String responseAsString = response.readEntity(String.class);

        ApiResult apiResult = mapper.readValue(responseAsString, ApiResult.class);

        return apiResult.team.games;

    }
}
