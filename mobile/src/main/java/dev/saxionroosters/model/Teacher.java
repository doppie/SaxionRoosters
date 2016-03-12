package dev.saxionroosters.model;

import java.io.Serializable;

/**
 * Created by Doppie on 22-2-2016.
 */
public class Teacher extends Owner implements Serializable {

    private String idName;

    public Teacher(String fullName, String idName) {
        super(fullName, OwnerType.TEACHER);
        this.idName = idName;
    }

    public String getIdName() {
        return idName;
    }
    
}
