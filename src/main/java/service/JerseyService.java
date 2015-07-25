package service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.Game;
import dto.Season;

public interface JerseyService {

    public Season[] readSeason(long seasonId) throws JsonParseException, JsonMappingException, IOException;

    public Game[] readGames(long teamId) throws JsonParseException, JsonMappingException, IOException;
}
