package nl.jelletenbrinke.saxionroosters.model;

import java.util.ArrayList;

/**
 * Created by Doppie on 24-2-2016.
 */
public class Week {

    private String name;
    private String id;
    private ArrayList<Day> days;

    public Week(String name, String id) {
        this.name = name;
        this.id = id;
        this.days = new ArrayList<>();
    }

    public Week(String name, String id, ArrayList<Day> days) {
        this.name = name;
        this.id = id;
        this.days = days;
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
