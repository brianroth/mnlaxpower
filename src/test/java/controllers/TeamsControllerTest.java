package controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Division;
import models.Game;
import models.Season;
import models.Team;
import ninja.Result;
import ninja.session.SessionImpl;
import ninja.utils.FakeContext;
import ninja.utils.MockNinjaProperties;
import ninja.utils.NinjaConstant;
import ninja.utils.NinjaProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dao.GameDao;
import dao.TeamDao;
import filters.OptionsFilter;

@RunWith(MockitoJUnitRunner.class)
public class TeamsControllerTest {

    @Mock
    private TeamDao teamDao;

    @Mock
    private GameDao gameDao;

    private TeamsController controller;

    private FakeContext context;

    @Before
    public void setUp() throws Exception {

        NinjaProperties ninjaProperties = MockNinjaProperties.createWithMode("test", "/",
                NinjaConstant.applicationCookiePrefix, "");

        context = new FakeContext();
        context.setSessionCookie(new SessionImpl(null, null, ninjaProperties));

        controller = new TeamsController();
        controller.setTeamDao(teamDao);
        controller.setGameDao(gameDao);
    }

    @Test
    public void testShow() throws ParseException {
        
    }
    
    @SuppressWarnings("unchecked")
    public void testShow2() throws ParseException {

        Season season = new Season(5678);

        Season[] seasons = new Season[] { season };

        Division division = new Division(1234, season.getId(), "Joy Division");

        Team team = new Team(90l, division.getId(), "Eagan");
        team.setDivision(division);

        List<Game> homeGames = new ArrayList<>();

        List<Game> awayGames = new ArrayList<>();

        when(teamDao.findById(team.getId())).thenReturn(team);
        when(gameDao.findByHomeTeamId(team.getId())).thenReturn(homeGames);
        when(gameDao.findByAwayTeamId(team.getId())).thenReturn(awayGames);

        Result result = controller.show(context, Long.toString(team.getId()));

        Map<String, Object> map = (Map<String, Object>) result.getRenderable();

        assertEquals(team, map.get("team"));
        assertEquals(team.getDivision(), map.get(OptionsFilter.DIVISION));
        assertEquals(team.getDivision().getId(), map.get("selectedDivision"));
        assertEquals(division.getSeasonId(), map.get("selectedSeason"));
        assertEquals(season, map.get(OptionsFilter.SEASON));
        assertEquals(seasons, map.get(OptionsFilter.SEASONS));
        assertEquals(200, result.getStatusCode());
    }
}
