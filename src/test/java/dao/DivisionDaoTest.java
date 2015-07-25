package dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import models.Division;

import org.junit.Test;

import com.google.inject.Inject;

public class DivisionDaoTest extends DaoTestCase {

    @Inject
    private DivisionDao divisionDao;

    @Inject
    private TeamDao teamDao;
    
    @Test
    public void testFindOrCreate() {
        Division expectedDivision = divisionDao.findOrCreate(1234, 5678, "Joy Division");

        teamDao.findOrCreate(1, 1234, "The first team");
        teamDao.findOrCreate(2, 1234, "The second team");
        
        assertNotNull(expectedDivision);

        Division division = divisionDao.findById(1234);

        assertNotNull(division);

        assertEquals(expectedDivision.getId(), division.getId());
        
        assertEquals(2, division.getTeams().size());
    }
}
