package controller.enums;

/**
 * RECIPIENT for message
 */
public enum RECIPIENT_TYPE {
    SKUPINA("Skupina"),
    UZIVATEL("Uživatel");

    private String value;

    RECIPIENT_TYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

