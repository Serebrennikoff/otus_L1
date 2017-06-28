package ru.otus.db_service;

import ru.otus.db_service.data_sets.UserDataSet;

import java.util.List;

/**
 * Database interface for application.
 */
public interface DBService {
    void save(UserDataSet user);

    UserDataSet findById(long id);

    UserDataSet findByName(String name);

    List<UserDataSet> findAll();

    void shutdown();
}
