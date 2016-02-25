package nl.jelletenbrinke.saxionroosters.model;

import java.util.ArrayList;

/**
 * Created by Doppie on 22-2-2016.
 */
public class Day {

    private ArrayList<College> colleges;
    private String name;

    public Day(String name, ArrayList<College> colleges) {
        this.name = name;
        this.colleges = colleges;
    }

    public Day(String name) {
        this.name = name;
        this.colleges = new ArrayList<>();
    }

    public ArrayList<College> getColleges() {
        return colleges;
    }

    public String getName() {
        return name;
    }
}
