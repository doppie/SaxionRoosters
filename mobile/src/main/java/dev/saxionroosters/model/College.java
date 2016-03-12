package dev.saxionroosters.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Doppie on 22-2-2016.
 */
public class College implements Serializable {

    private String name;
    private String type;
    private String time;
    private String location;
    private ArrayList<Teacher> teachers;

    private String date;
    private boolean showFreeDay = false;

    public College(String name, String type, String time, String location, ArrayList<Teacher> teachers) {
        this.name = name;
        this.type = type;
        this.time = time;
        this.location = location;
        //Make sure every location has its own line by adding \n to every whitespace, looks much better.
        if(this.location != null) this.location = this.location.replaceAll("\\s+", "\n");
        this.teachers = teachers;
    }

    public College(String date, boolean showFreeDay) {
        this.showFreeDay = showFreeDay;
        this.date = date;
        //the rest is null, because this is not really a college item
        //when this constructor is used it is a date divider for the schedule.
        //not the nicest hack. Pls fix.
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public boolean isFreeDay() {
        return showFreeDay;
    }

}
