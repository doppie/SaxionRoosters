package dev.saxionroosters.model;

import java.io.Serializable;

/**
 * Created by Doppie on 25-2-2016.
 */
public class Result implements Serializable {

    private String abbrevation;
    private String name;
    private String type;

    public String getAbbrevation() {
        return abbrevation;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Result(String abbrevation, String name, String type) {
        this.abbrevation = abbrevation;
        this.name = name;
        this.type = type;

    }
}