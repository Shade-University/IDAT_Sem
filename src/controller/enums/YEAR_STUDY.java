package controller.enums;

/**
 * Study year
 */
public enum YEAR_STUDY {
    FIRST_YEAR("1. ročník"),
    SECOND_YEAR("2. ročník"),
    THIRD_YEAR("3. ročník"),
    FOURTH_YEAR("4. ročník");

    private String value;
    YEAR_STUDY(String value){this.value = value;}

    public String getValue() {return value; }

    @Override
    public String toString() {
        return value;
    }
}
