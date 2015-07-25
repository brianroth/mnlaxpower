package dao;

import java.lang.reflect.ParameterizedType;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlecode.objectify.Objectify;

public abstract class BusinessObjectDao<T> {

    @Inject
    protected Provider<Objectify> objectify;

    public T save(T t) {
        objectify.get().save().entity(t).now();

        return t;
    }

    public T findById(long id) {
        return objectify.get().load().type(getType()).filter("id", id).first().now();
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType() {
        ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) superclass.getActualTypeArguments()[0];
    }
}
