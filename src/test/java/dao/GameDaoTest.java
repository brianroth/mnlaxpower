package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import models.Division;
import models.Game;
import models.Team;

import org.junit.Test;

import com.google.inject.Inject;

public class GameDaoTest extends DaoTestCase {

    @Inject
    private DivisionDao divisionDao;

    @Inject
    private TeamDao teamDao;

    @Inject
    private GameDao gameDao;

    @Test
    public void testFindOrCreate() {
        Division division = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        teamDao.findOrCreate(1, division.getId(), "The first team");
        teamDao.findOrCreate(2, division.getId(), "The second team");

        Game expectedGame = gameDao.findOrCreate(1, 1, 10, 2, 10, "7/23/2015 7:00Pm CDT", "Northview Elementary");

        assertNotNull(expectedGame);

        Game game = gameDao.findById(1);

        assertNotNull(game);

        assertEquals(expectedGame.getId(), game.getId());

        assertEquals(expectedGame.getHomeTeamId(), game.getHomeTeamId());
        assertEquals(expectedGame.getHomeTeam(), game.getHomeTeam());
        assertEquals(expectedGame.getHomeGoals(), game.getHomeGoals());

        assertEquals(expectedGame.getAwayTeamId(), game.getAwayTeamId());
        assertEquals(expectedGame.getAwayTeam(), game.getAwayTeam());
        assertEquals(expectedGame.getAwayGoals(), game.getAwayGoals());

        assertEquals(expectedGame.getDate(), game.getDate());
        assertEquals(expectedGame.getLocation(), game.getLocation());
    }

    @Test
    public void testFindByHomeTeamId() {
        Division division = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        Team homeTeam = teamDao.findOrCreate(1, division.getId(), "The home team");
        Team awayTeam1 = teamDao.findOrCreate(2, division.getId(), "The first away team");
        Team awayTeam2 = teamDao.findOrCreate(3, division.getId(), "The second away team");

        Game game1 = gameDao.findOrCreate(1, homeTeam.getId(), 10, awayTeam1.getId(), 10, "7/23/2015 7:00PM CDT", "Northview Elementary");
        Game game2 = gameDao.findOrCreate(2, homeTeam.getId(), 5, awayTeam2.getId(), 5, "7/24/2015 7:00PM CDT", "Northview Elementary");
        
        List<Game> games = gameDao.findByHomeTeamId(homeTeam.getId());
        
        assertNotNull(games);
        
        assertEquals(2, games.size());
        
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));

        games = gameDao.findByHomeTeamId(awayTeam1.getId());
        
        assertNotNull(games);
        
        assertEquals(0, games.size());

    }

    @Test
    public void testFindByAwayTeamId() {
        Division division = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        Team homeTeam = teamDao.findOrCreate(1, division.getId(), "The home team");
        Team awayTeam1 = teamDao.findOrCreate(2, division.getId(), "The first away team");
        Team awayTeam2 = teamDao.findOrCreate(3, division.getId(), "The second away team");

        Game game1 = gameDao.findOrCreate(1, homeTeam.getId(), 10, awayTeam1.getId(), 10, "7/23/2015 7:00PM CDT", "Northview Elementary");
        gameDao.findOrCreate(2, homeTeam.getId(), 5, awayTeam2.getId(), 5, "7/24/2015 7:00PM CDT", "Northview Elementary");
        
        game1 = gameDao.findById(1);
        
        List<Game> games = gameDao.findByAwayTeamId(awayTeam1.getId());
        
        assertNotNull(games);
        
        assertEquals(1, games.size());
        
        assertTrue(games.contains(game1));
    }
}