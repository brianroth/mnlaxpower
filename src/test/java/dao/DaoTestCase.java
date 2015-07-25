package dao;

import nl.pvanassen.guicejunitrunner.GuiceJUnitRunner;
import nl.pvanassen.guicejunitrunner.GuiceModules;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import service.JerseyService;
import service.JerseyServiceImpl;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.googlecode.objectify.Objectify;

import conf.ObjectifyProvider;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(DaoTestCase.DaoTestModule.class)
public abstract class DaoTestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Singleton
    public static class DaoTestModule extends AbstractModule {

        protected void configure() {
            // bind your Objectify.class to your provider like so:
            bind(Objectify.class).toProvider(ObjectifyProvider.class);
            bind(JerseyService.class).to(JerseyServiceImpl.class);
        }

    }
}
