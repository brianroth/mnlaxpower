package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Map;

import models.Division;
import models.Season;
import ninja.Result;
import ninja.session.SessionImpl;
import ninja.utils.FakeContext;
import ninja.utils.MockNinjaProperties;
import ninja.utils.NinjaConstant;
import ninja.utils.NinjaProperties;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dao.DivisionDao;
import filters.OptionsFilter;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationControllerTest {

    @Mock
    private DivisionDao divisionDao;

    private ApplicationController controller;

    private FakeContext context;

    @Before
    public void setUp() throws Exception {
        NinjaProperties ninjaProperties = MockNinjaProperties.createWithMode("test", "/",
                NinjaConstant.applicationCookiePrefix, "");

        context = new FakeContext();
        context.setSessionCookie(new SessionImpl(null, null, ninjaProperties));

        controller = new ApplicationController();
        controller.setDivisionDao(divisionDao);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIndexFromContext() throws ParseException {

        Season season = new Season(5678);

        Season[] seasons = new Season[] { season };

        Division division = new Division(1234, season.getId(), "Joy Division");
        division.setLastRecache(DateUtils.parseDate("2015:12:25 12:15:01", new String[] { "yyyy:MM:dd HH:mm:ss" }));
        division.setLastRecalculate(DateUtils.parseDate("2015:12:25 8:15:01", new String[] { "yyyy:MM:dd HH:mm:ss" }));
        context.setAttribute(OptionsFilter.SEASON, season);
        context.setAttribute(OptionsFilter.SEASONS, seasons);
        context.setAttribute(OptionsFilter.DIVISION, division);

        Result result = controller.index(context, null);

        assertEquals(200, result.getStatusCode());

        Map<String, Object> map = (Map<String, Object>) result.getRenderable();

        assertEquals(season, map.get(OptionsFilter.SEASON));
        assertEquals(seasons, map.get(OptionsFilter.SEASONS));
        assertEquals(division, map.get(OptionsFilter.DIVISION));
        assertEquals(division.getId(), map.get("selectedDivision"));
        assertEquals(division.getSeasonId(), map.get("selectedSeason"));
        assertEquals("2015-12-25 12:15 PM CST", map.get("scheduleLastUpdated"));
        assertEquals("2015-12-25 08:15 AM CST", map.get("rpiLastUpdated"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIndexWithParameter() throws ParseException {

        Season season = new Season(5678);

        Season[] seasons = new Season[] { season };

        Division division = new Division(1234, season.getId(), "Joy Division");
        division.setLastRecache(DateUtils.parseDate("2015:12:25 12:15:01", new String[] { "yyyy:MM:dd HH:mm:ss" }));
        division.setLastRecalculate(DateUtils.parseDate("2015:12:25 8:15:01", new String[] { "yyyy:MM:dd HH:mm:ss" }));

        when(divisionDao.findById(division.getId())).thenReturn(division);

        context.setAttribute(OptionsFilter.SEASON, season);
        context.setAttribute(OptionsFilter.SEASONS, seasons);

        assertNull(context.getSession().get(OptionsFilter.DIVISION_ID));

        Result result = controller.index(context, Long.toString(division.getId()));

        assertEquals(Long.toString(division.getId()), context.getSession().get(OptionsFilter.DIVISION_ID));

        assertEquals(200, result.getStatusCode());

        Map<String, Object> map = (Map<String, Object>) result.getRenderable();

        assertEquals(season, map.get(OptionsFilter.SEASON));
        assertEquals(seasons, map.get(OptionsFilter.SEASONS));
        assertEquals(division, map.get(OptionsFilter.DIVISION));
        assertEquals(division.getId(), map.get("selectedDivision"));
        assertEquals(division.getSeasonId(), map.get("selectedSeason"));
        assertEquals("2015-12-25 12:15 PM CST", map.get("scheduleLastUpdated"));
        assertEquals("2015-12-25 08:15 AM CST", map.get("rpiLastUpdated"));
    }
}
