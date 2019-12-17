package controller;

import model.Configuration;

import java.io.*;
import java.util.Properties;

/**
 * Configuration handler
 */
public class ConfigurationHandler {
    private String propFileName = "src/resources/configuration.properties";
    private File file;

    /**
     * Get configuration from property file
     * @return Configuration
     * @throws IOException
     */
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


    /**
     * Set properties values from configuration
     * @param config
     * @throws IOException
     */
    public void setPropValues(Configuration config) throws IOException {
        file = new File(propFileName);
        Properties prop = new Properties();
        prop.setProperty("USERNAME", config.getUsername());
        prop.setProperty("PASSWORD", config.getPassword());
        prop.setProperty("SERVER_NAME", config.getServer_name());
        prop.setProperty("PORT", String.valueOf(config.getPort()));
        prop.setProperty("DBMS", config.getDbms());
        prop.setProperty("SID", config.getSid());
        setPropValuesProp(prop);
    }

    /**
     * Save properties to file
     * @param config
     * @throws IOException
     */
    public void setPropValuesProp(Properties config) throws IOException {
        FileOutputStream fr = new FileOutputStream(file);
        config.store(fr, "Database configuration");
        fr.close();
        System.out.println("Saved: " + config);
    }
}
