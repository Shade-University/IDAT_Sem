package data;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


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

public class OracleConnection {
    
    private OracleConnection() {} //Singleton

    private static Connection connection = null;
    
    private static final String USERNAME = "<username>"; //Nejlepší by bylo tahat citlivá data z nějakýho zdroje
    private static final String PASSWORD = "<password>"; //heslo pro veřejnost
    private static final String SERVER_NAME = "fei-sql1.upceucebny.cz";
    private static final int PORT = 1521;
    private static final String DBMS = "oracle:thin";
    private static final String SID = "IDAS";

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            setUpConnection();
        }
        return connection;
    }

    private static void setUpConnection() throws SQLException {

        Properties connectionProps = new Properties();
        connectionProps.put("user", USERNAME);
        connectionProps.put("password", PASSWORD);

        connection = DriverManager.getConnection("jdbc:" + DBMS + ":@"
                + SERVER_NAME
                + ":" + PORT + ":" + SID,
                connectionProps);

        connection.setAutoCommit(false);

        System.out.println("Connected to database");
    }

    public static String getConnectionString() {
        return "//jdbc:" + DBMS+ ":@" + SERVER_NAME + ":" + PORT + ":" + SID;
    }

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
     * 
     * @param date
     * @param format
     * @return 
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
