package controller.enums;

public enum ACTION_TYPE {
    INSERT("Vložit"),
    UPDATE("Aktualizovat"),
    DELETE("Smazat");

    private String value;

    ACTION_TYPE(String value) {
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
