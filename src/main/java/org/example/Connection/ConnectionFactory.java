package org.example.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory class for managing database connections and resources.
 */
public class ConnectionFactory {

    //private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://localhost:5432/pt";
    private static final String USER = "postgres";
    private static final String PASS = "Luisa211003";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
//            if(connection != null) {
//                System.out.println("ok");
//            } else {
//                System.out.println("fail");
//            }
        } catch (SQLException e) {
            //LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Retrieves a connection to the database.
     *
     * @return A Connection object representing the database connection.
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes the database connection.
     *
     * @param connection The Connection object to close.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                //LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the database statement.
     *
     * @param statement The Statement object to close.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                //LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the database result set.
     *
     * @param resultSet The ResultSet object to close.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                //LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
                e.printStackTrace();
            }
        }
    }
}
