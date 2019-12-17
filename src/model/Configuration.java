package model;

/**
 * Database configuration model
 */
public class Configuration {
    private String username;
    private String password;
    private String server_name;
    private int port;
    private String dbms;
    private String sid;

    public Configuration(String username, String password, String server_name, int port, String dbms, String sid) {
        this.username = username;
        this.password = password;
        this.server_name = server_name;
        this.port = port;
        this.dbms = dbms;
        this.sid = sid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
