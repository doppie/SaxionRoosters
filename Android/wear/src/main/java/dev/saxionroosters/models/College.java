package dev.saxionroosters.models;

/**
 * Created by Wessel on 17-11-2016.
 */

public class College {

    public String title;
    public String room;
    public String time;
    public String date;
    public int color;

    public College(String title, String room, String time, String date, int color) {
        this.title = title;
        this.room = room;
        this.time = time;
        this.date = date;
        this.color = color;
    }

}