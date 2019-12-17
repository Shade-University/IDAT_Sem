package model;

public class Configuration {
    private String USERNAME;
    private String PASSWORD;
    private String SERVER_NAME;
    private int PORT;
    private String DBMS;
    private String SID;

    public Configuration(String USERNAME, String PASSWORD, String SERVER_NAME, int PORT, String DBMS, String SID) {
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.SERVER_NAME = SERVER_NAME;
        this.PORT = PORT;
        this.DBMS = DBMS;
        this.SID = SID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getSERVER_NAME() {
        return SERVER_NAME;
    }

    public void setSERVER_NAME(String SERVER_NAME) {
        this.SERVER_NAME = SERVER_NAME;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getDBMS() {
        return DBMS;
    }

    public void setDBMS(String DBMS) {
        this.DBMS = DBMS;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }
}
