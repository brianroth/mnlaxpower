package dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public abstract class BusinessObjectDao<T> {

    @Inject
    protected Provider<Objectify> objectify;
    
    public T save(T t) {
        objectify.get().save().entity(t);

        return t;
    }
}
