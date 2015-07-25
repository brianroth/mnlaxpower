package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import models.Division;
import models.Team;

import org.junit.Test;

import com.google.inject.Inject;

public class TeamDaoTest extends DaoTestCase {

    @Inject
    private DivisionDao divisionDao;

    @Inject
    private TeamDao teamDao;

    @Test
    public void testFindAllByDivision() {
        Division division = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        Team team1 = teamDao.findOrCreate(1, 1234, "The first team");
        team1.setRpi(0.4);
        teamDao.save(team1);

        Team team2 = teamDao.findOrCreate(2, 1234, "The second team");
        team2.setRpi(0.5);
        teamDao.save(team2);

        List<Team> teams = teamDao.findAllByDivision(division.getId());

        assertNotNull(teams);

        assertEquals(2, teams.size());

        assertEquals(team2, teams.get(0));
        assertEquals(team1, teams.get(1));
    }

    @Test
    public void testFindOrCreate() {
        Division division = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        Team expectedTeam = teamDao.findOrCreate(1, division.getId(), "The first team");

        assertNotNull(expectedTeam);

        Team Team = teamDao.findById(1);

        assertNotNull(Team);

        assertEquals(expectedTeam.getId(), Team.getId());
        assertEquals(expectedTeam.getName(), Team.getName());
        assertEquals(division, Team.getDivision());

    }

}
