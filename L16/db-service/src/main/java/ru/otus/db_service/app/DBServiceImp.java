package ru.otus.db_service.app;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.stat.Statistics;
import ru.otus.db_service.dao.UserDAO;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * DBService interface implementation backed by message system.
 */
public class DBServiceImp implements DBService {
    private static final Logger logger = Logger.getLogger(DBServiceImp.class.getName());

    private static final String CREATE_DB_SERVER_FLAG = "-f";

    private SessionFactory factory;

    public DBServiceImp(String createDBServerFlag) {
        boolean isFlagged = createDBServerFlag.equals(CREATE_DB_SERVER_FLAG);
        String configResource = (isFlagged) ?
                "hibernate_create_server.cfg.xml" :
                "hibernate_common.cfg.xml";
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(configResource)
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        // Enable cache statistics
        factory.getStatistics().setStatisticsEnabled(true);
        // Start tcp server if required
        if (isFlagged) {
            try {
                Server.createTcpServer().start();
                logger.info("TCP server created.");
                populateDB();
                logger.info("DB populated.");
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
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

    private void populateDB() {
        UserDataSet[] users = {
                new UserDataSet("Peter",
                        new AddressDataSet("Baker str.", 142049),
                        new PhoneDataSet(234, "321-234"),
                        new PhoneDataSet(434, "188-990")),

                new UserDataSet("Chris",
                        new AddressDataSet("Fine str.", 143045),
                        new PhoneDataSet(234, "311-834")),

                new UserDataSet("Shone",
                        new AddressDataSet("Baker str.", 142049),
                        new PhoneDataSet(234, "241-234")),

                new UserDataSet("Bruce",
                        new AddressDataSet("Baum str.", 322049),
                        new PhoneDataSet(433, "184-933"),
                        new PhoneDataSet(433, "324-553"))
        };

        for (UserDataSet user : users) {
            save(user);
        }
    }
}
