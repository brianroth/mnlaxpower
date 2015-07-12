package controllers;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import models.Division;
import models.Metrics;
import models.Season;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.appengine.AppEngineFilter;
import ninja.params.PathParam;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.DivisionDao;
import dao.MetricsDao;
import dao.SeasonDao;

@Singleton
@FilterWith(AppEngineFilter.class)
public class ApplicationController {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm a z");

    private final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Inject
    private SeasonDao seasonDao;
    
    @Inject
    private DivisionDao divisionDao;

    @Inject
    private MetricsDao metricsDao;

    public Result index(@PathParam("d") String divisionId) {

        logger.info("param d = {}", divisionId);

        Season season = seasonDao.findById(12179);

        long selectedDivision = 18301;

        if (StringUtils.isNumeric(divisionId)) {
            selectedDivision = Long.parseLong(divisionId);
        }

        Division division = divisionDao.findById(selectedDivision);

        Result result = Results.html();

        Metrics metrics = metricsDao.find();

        if (null != metrics.getLastRecache()) {
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("CDT"));
            result.render("scheduleLastUpdated", DATE_FORMAT.format(metrics.getLastRecache()));
        }

        if (null != metrics.getLastRecalculate()) {
            DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("CDT"));
            result.render("rpiLastUpdated", DATE_FORMAT.format(metrics.getLastRecalculate()));
        }

        result.render("season", season);
        result.render("division", division);
        result.render("selectedDivision", selectedDivision);

        return result;
    }

    public Result faq() {
        return Results.html();
    }
}
