package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import models.Division;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;
import ninja.session.Session;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.DivisionDao;
import filters.OptionsFilter;

@Singleton
@FilterWith({ AppEngineFilter.class, OptionsFilter.class })
public class ApplicationController {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm a z");

    @Inject
    private DivisionDao divisionDao;

    public Result index(Context context, @PathParam("divisionId") String divisionId) {

        Session session = context.getSession();

        long selectedDivision = Long.parseLong(session.get(OptionsFilter.DIVISION_ID));

        Division division = (Division) context.getAttribute(OptionsFilter.DIVISION);

        if (StringUtils.isNumeric(divisionId)) {
            selectedDivision = Long.parseLong(divisionId);
            session.put(OptionsFilter.DIVISION_ID, divisionId);
            division = divisionDao.findById(selectedDivision);
        }

        Result result = Results.html();

        renderDate(result, "scheduleLastUpdated", division.getLastRecache());
        renderDate(result, "rpiLastUpdated", division.getLastRecalculate());

        result.render("seasons", context.getAttribute(OptionsFilter.SEASONS));
        result.render("divisions", context.getAttribute(OptionsFilter.DIVISIONS));

        result.render("season", context.getAttribute(OptionsFilter.SEASON));
        result.render("division", division);
        result.render("selectedDivision", selectedDivision);
        result.render("selectedSeason", context.getAttribute(OptionsFilter.SEASON_ID));

        return result;
    }

    public Result faq(Context context) {
        Result result = Results.html();

        result.render("season", context.getAttribute(OptionsFilter.SEASON));
        result.render("division", context.getAttribute(OptionsFilter.DIVISION));

        result.render("seasons", context.getAttribute(OptionsFilter.SEASONS));
        result.render("divisions", context.getAttribute(OptionsFilter.DIVISIONS));

        result.render("selectedDivision", context.getAttribute(OptionsFilter.DIVISION_ID));
        result.render("selectedSeason", context.getAttribute(OptionsFilter.SEASON_ID));

        return result;
    }

    private void renderDate(Result result, String key, Date value) {
        if (null != value) {
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("US/Central"));
            result.render(key, DATE_FORMAT.format(value));
        }
    }
}
