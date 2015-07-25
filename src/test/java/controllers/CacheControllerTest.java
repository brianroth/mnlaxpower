package controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import models.Division;
import models.Team;
import ninja.Result;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import service.JerseyService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dao.DivisionDao;
import dao.GameDao;
import dao.SeasonDao;
import dao.TeamDao;
import dto.Season;
import filters.OptionsFilter;

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerTest {

    @Mock
    private JerseyService apiService;

    @Mock
    private SeasonDao seasonDao;

    @Mock
    private DivisionDao divisionDao;

    @Mock
    private TeamDao teamDao;

    @Mock
    private GameDao gameDao;

    private CacheController controller;

    @Before
    public void setUp() throws Exception {
        controller = new CacheController();
        controller.setDivisionDao(divisionDao);
        controller.setSeasonDao(seasonDao);
        controller.setGameDao(gameDao);
        controller.setTeamDao(teamDao);
        controller.setApiService(apiService);
    }

    @Test
    public void testRecalculate() throws ParseException {

        models.Season season = new models.Season(OptionsFilter.DEFAULT_SEASON_ID);

        Division division = new Division(1234, season.getId(), "Joy Division");
        List<Division> divisions = new ArrayList<>();
        divisions.add(division);

        season.setDivisions(divisions);

        List<Team> teams = new ArrayList<>();

        when(seasonDao.findById(OptionsFilter.DEFAULT_SEASON_ID)).thenReturn(season);
        when(teamDao.findAllByDivision(division.getId())).thenReturn(teams);

        Result result = controller.recalculate();

        assertEquals(303, result.getStatusCode());
    }

    @Test
    public void testRecacheNoDivision() throws ParseException, JsonParseException, JsonMappingException, IOException {

        Division division = new Division(1234, 5678, "Joy Division");

        Season[] seasons = new Season[] {};

        when(apiService.readSeason(division.getSeasonId())).thenReturn(seasons);

        Result result = controller.recache(Long.toString(OptionsFilter.DEFAULT_SEASON_ID));

        assertEquals(303, result.getStatusCode());
    }

    @Test
    public void testRecacheWithDivision() throws ParseException {

        Division division = new Division(1234, 5678, "Joy Division");
        division.setTeams(new ArrayList<Team>());

        when(divisionDao.findById(division.getId())).thenReturn(division);

        Result result = controller.recache(Long.toString(division.getId()));

        assertEquals(303, result.getStatusCode());
    }

}
