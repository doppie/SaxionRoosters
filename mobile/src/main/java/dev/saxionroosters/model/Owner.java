package dev.saxionroosters.model;

import java.io.Serializable;

import dev.saxionroosters.extras.S;

/**
 * Created by Doppie on 26-2-2016.
 */
public class Owner implements Serializable {

    public static enum OwnerType { GROUP, TEACHER, COURSE, ACADEMY }

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
            case COURSE: return S.COURSE;
            case ACADEMY: return S.ACADEMY;
            default: return "UNKNOWN";
        }
    }
}
