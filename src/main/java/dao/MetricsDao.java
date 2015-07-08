package dao;

import models.Metrics;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public class MetricsDao {
    @Inject
    private Provider<Objectify> objectify;

    public Metrics find() {
        Metrics metrics = objectify.get().load().type(Metrics.class).first().now();

        if (null == metrics) {

            metrics = new Metrics();
            objectify.get().save().entity(metrics);
        }

        return metrics;
    }

    public Metrics save(Metrics metrics) {
        objectify.get().save().entity(metrics);

        return metrics;
    }
}
