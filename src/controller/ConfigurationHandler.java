package controller;

import model.Configuration;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class ConfigurationHandler {
    private String propFileName = "src/resources/configuration.properties";
    private File file;

    public Configuration getPropValues() throws IOException {
        try {
            Properties prop = new Properties();
            file = new File(propFileName);
            FileInputStream fi = new FileInputStream(file);
            prop.load(fi);
            fi.close();
            System.out.println("Loaded: " + prop);
            Configuration config = new Configuration(
                    prop.getProperty("USERNAME"),
                    prop.getProperty("PASSWORD"),
                    prop.getProperty("SERVER_NAME"),
                    Integer.valueOf(prop.getProperty("PORT")),
                    prop.getProperty("DBMS"),
                    prop.getProperty("SID")
            );
            return config;
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }



    public void setPropValues(Configuration config) throws IOException {
        file = new File(propFileName);
        Properties prop = new Properties();
        prop.setProperty("USERNAME", config.getUSERNAME());
        prop.setProperty("PASSWORD", config.getPASSWORD());
        prop.setProperty("SERVER_NAME", config.getSERVER_NAME());
        prop.setProperty("PORT", String.valueOf(config.getPORT()));
        prop.setProperty("DBMS", config.getDBMS());
        prop.setProperty("SID", config.getSID());
        setPropValuesProp(prop);
    }

    public void setPropValuesProp(Properties config) throws IOException {
        FileOutputStream fr = new FileOutputStream(file);
        config.store(fr, "Database configuration");
        fr.close();
        System.out.println("Saved: " + config);
    }
}
