package ru.otus.main;

import org.h2.tools.Server;
import ru.otus.cache.CacheEngine;
import ru.otus.cache.CacheEngineQImpl;
import ru.otus.db_service.DBService;
import ru.otus.db_service.DBServiceImp;
import ru.otus.db_service.data_sets.AddressDataSet;
import ru.otus.db_service.data_sets.PhoneDataSet;
import ru.otus.db_service.data_sets.UserDataSet;

import java.util.List;

/**
 * ...
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // Server.createWebServer("-web","-webAllowOthers","-webPort","8082").start();
        CacheEngine<Long, UserDataSet> cache = new CacheEngineQImpl<>(2, 10000, 2000);
        DBService dbService = new DBServiceImp(cache);

        dbService.save(new UserDataSet("Peter",
                new AddressDataSet("Baker str.", 142049),
                new PhoneDataSet(234, "321-234")));
        dbService.save(new UserDataSet("Mark",
                new AddressDataSet("Himark str.", 122039),
                new PhoneDataSet(434, "431-431"),
                new PhoneDataSet(434, "768-890")));

        System.out.println("====================================");
        System.out.println("Find user by name:");
        UserDataSet userByName = dbService.findByName("Mark");
        System.out.println(userByName);
        System.out.println("====================================");

        dbService.findById(1); // hit No. 1
        dbService.save(new UserDataSet("Helen",
                new AddressDataSet("Corner str.", 134049),
                new PhoneDataSet(314, "451-864")));
        dbService.findById(2); // miss No. 1
        dbService.findById(3); // hit No. 2

        System.out.println("Hit count: " + cache.getHitCount() + "\n" + "Miss count: " + cache.getMissCount());
        System.out.println("=====================================");

        List<UserDataSet> allUsers = dbService.findAll();
        for (UserDataSet user : allUsers) {
            System.out.println(user);
        }

        dbService.shutdown();
    }
}
