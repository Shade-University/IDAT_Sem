package controller.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * INSTITUTE
 */
public enum INSTITUTE {
    KE("Katedra elektrotechniky"),
    KIT("Katedra informačních technologií"),
    KMF("Katedra matematiky a fyziky"),
    KRP("Katedra řízení procesů"),
    KST("Katedra matematiky a fyziky");

    private String value;
    INSTITUTE(String value){this.value = value;}

    public String getValue() {return value; }

    @Override
    public String toString() {
        return value;
    }

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, INSTITUTE> lookup = new HashMap<>();

    static
    {
        for(INSTITUTE env : INSTITUTE.values())
        {
            lookup.put(env.getValue(), env);
        }
    }

    public static INSTITUTE get(String type)
    {
        return lookup.get(type);
    }
}
