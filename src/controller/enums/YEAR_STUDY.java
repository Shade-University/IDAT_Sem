package controller.enums;

import java.util.HashMap;
import java.util.Map;

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

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, YEAR_STUDY> lookup = new HashMap<>();

    static
    {
        for(YEAR_STUDY env : YEAR_STUDY.values())
        {
            lookup.put(env.getValue(), env);
        }
    }

    public static YEAR_STUDY get(String type)
    {
        return lookup.get(type);
    }
}
