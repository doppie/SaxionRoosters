package dev.saxionroosters.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Doppie on 24-2-2016.
 */
public class Week implements Serializable {

    private ArrayList<Day> days;
    private String name;
    private String id;
    private Owner owner;

    public Week(Owner owner, String name, String id, ArrayList<Day> days) {
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.days = days;
    }

    public Week(Owner owner, String name, String id) {
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.days = new ArrayList<>();
    }

    public Owner getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Day> getDays() {
        return days;
    }
}
