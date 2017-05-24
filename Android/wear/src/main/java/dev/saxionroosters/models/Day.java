package dev.saxionroosters.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Doppie on 22-2-2016.
 */
public class Day implements Serializable {

    private String date;
    private ArrayList<College> colleges;


    public Day(String date, ArrayList<College> colleges) {
        this.date = date;
        this.colleges = colleges;
    }

    public Day(String date) {
        this.date = date;
        this.colleges = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public ArrayList<College> getColleges() {
        return colleges;
    }


}
