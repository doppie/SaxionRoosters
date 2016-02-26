package nl.jelletenbrinke.saxionroosters.model;

import java.util.ArrayList;

/**
 * Created by Doppie on 24-2-2016.
 */
public class Week {

    private String name;
    private String id;
    private ArrayList<Day> days;
    private String owner;
    private String ownerType;

    public Week(String name, String id, String owner, String ownerType) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.ownerType = ownerType;
        this.days = new ArrayList<>();
    }

    public String getOwnerType() {
        return ownerType;
    }

    public Week(String name, String id, String owner, String ownerType, ArrayList<Day> days) {
        this.name = name;
        this.id = id;
        this.owner = owner;
        this.ownerType = ownerType;
        this.days = days;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}
