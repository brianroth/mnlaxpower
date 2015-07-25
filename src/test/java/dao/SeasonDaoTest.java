package dao;

import models.Season;

import org.junit.Test;

import com.google.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SeasonDaoTest extends DaoTestCase {

    @Inject
    private SeasonDao seasonDao;

    @Test
    public void testFindOrCreate() {
        Season expectedSeason = seasonDao.findOrCreate(1, "Summer Season");

        assertNotNull(expectedSeason);
        
        Season season = seasonDao.findById(1);

        assertNotNull(season);
        
        assertEquals(expectedSeason.getId(), season.getId());
        assertEquals(expectedSeason.getName(), season.getName());
    }

}
