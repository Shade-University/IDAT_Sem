package controller.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * User role
 */

public enum USER_TYPE {
    ADMIN("admin"),
    STUDENT("student"),
    TEACHER("ucitel");

    private String type;

    USER_TYPE(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    //listView zobrazení enumu
    @Override
    public String toString() {return type;}

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, USER_TYPE> lookup = new HashMap<>();

    static
    {
        for(USER_TYPE env : USER_TYPE.values())
        {
            lookup.put(env.getType(), env);
        }
    }

    public static USER_TYPE get(String type)
    {
        return lookup.get(type);
    }
}
