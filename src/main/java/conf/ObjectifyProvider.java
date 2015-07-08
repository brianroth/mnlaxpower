package conf;

import models.Division;
import models.Game;
import models.Metrics;
import models.Season;
import models.Team;

import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyProvider implements Provider<Objectify> {

    static {
        ObjectifyService.register(Division.class);
        ObjectifyService.register(Team.class);
        ObjectifyService.register(Season.class);
        ObjectifyService.register(Game.class);
        ObjectifyService.register(Metrics.class);
    }

    @Override
    public Objectify get() {
        return ObjectifyService.ofy();
    }
}
