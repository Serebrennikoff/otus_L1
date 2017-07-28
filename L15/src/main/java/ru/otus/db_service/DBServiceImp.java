package ru.otus.db_service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.dao.UserDAO;
import ru.otus.db_service.data_sets.UserDataSet;
import ru.otus.message_system.Address;
import ru.otus.message_system.Addressee;
import ru.otus.message_system.MessageSystemContext;

import java.util.List;
import java.util.function.Function;

/**
 * DBService interface implementation backed by message system.
 */
public class DBServiceImp implements DBService, Addressee {
    private final Address address;
    private final MessageSystemContext context;

    private SessionFactory factory;

    public DBServiceImp(MessageSystemContext context, Address address) {
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

        this.address = address;
        this.context = context;
        register();
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


    @Override
    public Address getAddress() {
        return address;
    }

    private void register() {
        context.getMessageSystem().addAddressee(this);
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
