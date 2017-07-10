package ru.otus.db_service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.dao.UserDAO;
import ru.otus.db_service.data_sets.UserDataSet;

import java.util.List;
import java.util.function.Function;

/**
 * DBService interface implementation.
 */
public class DBServiceImp implements DBService {
    private SessionFactory factory;

    public DBServiceImp() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            // StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException();
        }
        // Enable cache statistics
        factory.getStatistics().setStatisticsEnabled(true);
    }

    @Override
    public void save(UserDataSet user) {
        performTransaction(session -> {
            new UserDAO(session).save(user);
            return true;
        });
    }

    @Override
    public UserDataSet findById(long id) {
        return performTransaction(session -> new UserDAO(session).find(id));
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
    public void shutdown() { factory.close(); }

    @Override
    public long getCacheHitNum() {
        return factory.getStatistics().getSecondLevelCacheHitCount();
    }

    @Override
    public long getCacheMissNum() {
        return factory.getStatistics().getSecondLevelCacheMissCount();
    }

    @Override
    public Statistics getStatistics() {
        return factory.getStatistics();
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
