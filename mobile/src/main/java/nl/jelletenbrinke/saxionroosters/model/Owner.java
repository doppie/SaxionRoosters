package nl.jelletenbrinke.saxionroosters.model;

import nl.jelletenbrinke.saxionroosters.extras.S;

/**
 * Created by Doppie on 26-2-2016.
 */
public class Owner {

    public static enum OwnerType { GROUP, TEACHER }

    private String name;
    private OwnerType type;

    public Owner(String name, OwnerType type) {
        this.name = name;
        this.type = type;
    }

    public Owner(OwnerType type) {
        this.name = null;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public OwnerType getType() {
        return type;
    }

    public String getTypeName() {
        switch (type) {
            case GROUP: return S.GROUP;
            case TEACHER: return S.TEACHER;
            default: return "UNKNOWN";
        }
    }
}
