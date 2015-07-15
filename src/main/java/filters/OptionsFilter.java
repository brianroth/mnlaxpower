package filters;

import models.Division;
import models.Season;
import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;
import ninja.session.Session;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import dao.DivisionDao;
import dao.SeasonDao;

public class OptionsFilter implements Filter {

    public static final String SEASON = "season";

    public static final String DIVISION = "division";

    public static final String DIVISIONS = "divisions";

    public static final String SEASONS = "seasons";

    public static final String SEASON_ID = "seasonId";

    public static final String DIVISION_ID = "divisionId";

    public static final long DEFAULT_DIVISION_ID = 18301;

    public static final long DEFAULT_SEASON_ID = 12179;

    @Inject
    private SeasonDao seasonDao;

    @Inject
    private DivisionDao divisionDao;
    
    @Override
    public Result filter(FilterChain filterChain, Context context) {

        Session session = context.getSession();

        long divisionId = DEFAULT_DIVISION_ID;

        if (StringUtils.isNumeric(session.get(DIVISION_ID))) {
            divisionId = Long.parseLong(session.get(DIVISION_ID));
        }

        Division division = divisionDao.findById(divisionId);
        
        long seasonId = DEFAULT_SEASON_ID;

        if (StringUtils.isNumeric(session.get(SEASON_ID))) {
            seasonId = Long.parseLong(session.get(SEASON_ID));
        }

        session.put(SEASON_ID, Long.toString(seasonId));
        session.put(DIVISION_ID, Long.toString(divisionId));

        Season season = seasonDao.findById(seasonId);

        context.setAttribute(SEASON, season);
        context.setAttribute(DIVISION, division);
        
        context.setAttribute(DIVISIONS, season.getDivisions());
        context.setAttribute(SEASONS, seasonDao.findAll());
        
        context.setAttribute(SEASON_ID, seasonId);
        context.setAttribute(DIVISION_ID, divisionId);
        
        return filterChain.next(context);
    }
}