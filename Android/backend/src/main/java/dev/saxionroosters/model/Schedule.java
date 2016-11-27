package dev.saxionroosters.model;

import java.util.ArrayList;

/**
 * Created by jelle on 27/11/2016.
 */

public class Schedule {

    private Subject subject;
    private Week week;
    private ArrayList<Day> days;

    public Subject getSubject() {
        return subject;
    }

    public Week getWeek() {
        return week;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

}
