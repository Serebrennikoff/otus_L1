package ru.otus.l91.main;

import ru.otus.l91.db_workers.DBSession;
import ru.otus.l91.db_workers.Executor;
import ru.otus.l91.entities.user.User;
import ru.otus.l91.entities.user.UserDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * mysql> CREATE USER 'tully'@'localhost' IDENTIFIED BY 'tully';
 * mysql> GRANT ALL PRIVILEGES ON * . * TO 'tully'@'localhost';
 * mysql> select user, host from mysql.user;
 * mysql> create database db_example;
 * mysql> SET GLOBAL time_zone = '+1:00';
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Connection connection = DBSession.getConnection();
        Executor executor = new Executor(connection);
        UserDAO udao = new UserDAO(executor);

        try {
            executor.update("create table users (id bigint(20) auto_increment, user_name varchar(256), age int(3), primary key (id));");
            System.out.println("Table USERS created");

            User user = udao.createUser("Mary", 34);
            System.out.println("User " + user.getName() + " created");
            System.out.println("User id: " + user.getId());

            user = udao.createUser("Brad", 25);
            System.out.println("User " + user.getName() + " created");
            System.out.println("User id: " + user.getId());

            user = udao.createUser("Peter", 19);
            System.out.println("User " + user.getName() + " created");
            System.out.println("User id: " + user.getId());

            System.out.println("Looking for user with id 2:");
            user = udao.findUser(2);
            System.out.println(String.format("User with id 2 was found:%n name - %s,%n age - %s.", user.getName(), user.getAge()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                executor.update("drop table users");
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            System.out.println("Done!");
        }
    }
}
