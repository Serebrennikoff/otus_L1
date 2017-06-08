package ru.otus.l91.entities.user;

import ru.otus.l91.db_workers.Executor;

import java.sql.SQLException;

/**
 * User data access object.
 */
public class UserDAO {
    private Executor ex;

    public UserDAO(Executor ex) {
        this.ex = ex;
    }

    public User createUser(String name, int age) throws Exception {
        User user = new User(name, age);
        long id = ex.create(user);
        user.setId(id);
        return user;
    }

    public User findUser(long id) throws Exception {
        return ex.find(User.class, id, resultSet -> {
            User user = null;
            try {
                resultSet.next();
                user = new User(resultSet.getString(2), resultSet.getInt(3));
                user.setId(resultSet.getLong(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        });
    }

}
