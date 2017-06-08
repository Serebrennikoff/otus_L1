package ru.otus.l91.db_workers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton for database connection.
 */
public class DBSession {
    private static Connection connection;
    private static final String URL = "jdbc:mysql://" +             //db type
                                      "localhost:" +                //host name
                                      "3306/" +                     //port
                                      "db_example?" +               //db name
                                      "useSSL=false&" +             //do not use ssl
                                      "user=tully&" +               //login
                                      "password=tully";             //password

    private DBSession() {}

    public static Connection getConnection() throws SQLException {
        if(connection == null) {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(URL);
            return connection;
        }
        return connection;
    }
}
