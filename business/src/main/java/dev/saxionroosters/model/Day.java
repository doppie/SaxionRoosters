package dev.saxionroosters.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jelle on 27/11/2016.
 */

public class Day {

    private Date date;

    @SerializedName("entries")
    private ArrayList<College> colleges;

    public Date getDate() {
        return date;
    }

    public ArrayList<College> getColleges() {
        return colleges;
    }
}
