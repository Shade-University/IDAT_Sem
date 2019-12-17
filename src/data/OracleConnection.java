package data;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import controller.ConfigurationHandler;
import model.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Db connector class
 */
public class OracleConnection {

    private OracleConnection() {} //Singleton

    private static Connection connection = null;
    private static model.Configuration configuration = null;

    /**
     * Get connection to db
     * @return Connection to db
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            ConfigurationHandler cf = new ConfigurationHandler();
            try {
                setUpConnection(cf.getPropValues());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Test connection
     * @return Connection
     * @throws SQLException
     */
    public static Connection testConnection() throws SQLException {
            ConfigurationHandler cf = new ConfigurationHandler();
            try {
                setUpConnection(cf.getPropValues());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return connection;
    }

    /**
     * Set up connection to db
     * Load connection from config
     * @param config
     * @throws SQLException
     */
    private static void setUpConnection(Configuration config) throws SQLException {

        configuration = config;
        Properties connectionProps = new Properties();
        connectionProps.put("user", config.getUsername());
        connectionProps.put("password", config.getPassword());

        connection = DriverManager.getConnection("jdbc:" + config.getDbms() + ":@"
                        + config.getServer_name()
                        + ":" + config.getPort() + ":" + config.getSid(),
                connectionProps);

        connection.setAutoCommit(false);

        System.out.println("Connected to database");
    }

    /**
     * Get connection string to db
     * @return Connection String
     */
    public static String getConnectionString() {
        return "//jdbc:" + configuration.getDbms()+ ":@" + configuration.getServer_name() + ":" + configuration.getPort() + ":" + configuration.getSid();
    }

    /**
     * Close connection to db
     * @param commit
     * @throws SQLException
     */
    public static void closeConnection(boolean commit) throws SQLException {
        if (connection != null) {
            if (commit) {
                connection.commit();
            }

            connection.close();
            connection = null;
            System.out.println("Connection closed");
        }
    }
    /**
     * Helper method for parse date to db
     * @param date
     * @param format
     * @return sql.Date
     */
    public static java.sql.Date parseDate(String date, String format){
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        java.sql.Date result = null;
        try {
            result = new java.sql.Date(df.parse(date).getTime());
        } catch (ParseException ex) {
            Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}