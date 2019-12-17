package controller.enums;

/**
 * Import options from REST api
 */
public enum IMPORT_OPTION {
    SUBJECTS("Předměty"),
    FIELDS("Obory");

    private String value;
    IMPORT_OPTION(String value){this.value = value;}

    public String getValue() {return value; }

    @Override
    public String toString() {
        return value;
    }
}
