package controller.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Database operation action for ISKAM
 */
public enum DATABASE_OPERATION_TYPE {
    INSERT("Přidat"),
    UPDATE("Upravit"),
    DELETE("Odebrat");

    private String type;

    DATABASE_OPERATION_TYPE(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString() {return type;}

    private static final Map<String, DATABASE_OPERATION_TYPE> lookup = new HashMap<>();

    static
    {
        for(DATABASE_OPERATION_TYPE env : DATABASE_OPERATION_TYPE.values())
        {
            lookup.put(env.getType(), env);
        }
    }

    public static DATABASE_OPERATION_TYPE get(String type)
    {
        return lookup.get(type);
    }
}
