package ru.otus.db_service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.otus.cache.CacheEngine;
import ru.otus.db_service.dao.UserDAO;
import ru.otus.db_service.data_sets.UserDataSet;

import java.util.List;
import java.util.function.Function;

/**
 * DBService interface implementation.
 */
public class DBServiceImp implements DBService {
    private SessionFactory factory;
    private CacheEngine<Long, UserDataSet> cache;

    public DBServiceImp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public DBServiceImp(CacheEngine<Long, UserDataSet> cache) {
        this.cache = cache;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public void save(UserDataSet user) {
        performTransaction(session -> {
            long key = new UserDAO(session).save(user);
            if(cache != null) {
                user.setId(key);
                cache.put(key, user);
            }
            return true;
        });
    }

    @Override
    public UserDataSet findById(long id) {
        UserDataSet res = null;
        if(cache != null) {
            res = cache.get(id);
        }
        return (res == null) ? performTransaction(session -> new UserDAO(session).find(id)) : res;
    }

    @Override
    public UserDataSet findByName(String name) {
        return performTransaction(session -> new UserDAO(session).findByName(name));
    }

    @Override
    public List<UserDataSet> findAll() {
        return performTransaction(session -> new UserDAO(session).findAll());
    }

    @Override
    public void shutdown() {
        factory.close();
    }

    private <R> R performTransaction(Function<Session, R> func) {
        try(Session session = factory.openSession()) {
            session.beginTransaction();
            R res = func.apply(session);
            session.getTransaction().commit();
            return res;
        }
    }
}
