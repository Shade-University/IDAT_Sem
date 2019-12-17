package controller.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Transaction type for ISKAM
 */
public enum TRANSACTION_TYPE {
    IN("Příjem"),
    OUT("Výdaj");

    private String type;

    TRANSACTION_TYPE(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString() {return type;}

    private static final Map<String, TRANSACTION_TYPE> lookup = new HashMap<>();

    static
    {
        for(TRANSACTION_TYPE env : TRANSACTION_TYPE.values())
        {
            lookup.put(env.getType(), env);
        }
    }

    public static TRANSACTION_TYPE get(String type)
    {
        return lookup.get(type);
    }
}
