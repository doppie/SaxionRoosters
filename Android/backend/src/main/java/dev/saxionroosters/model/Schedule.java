package dev.saxionroosters.model;

import java.util.ArrayList;

/**
 * Created by jelle on 27/11/2016.
 */

public class Schedule {

    private Subject subject;
    private Week week;
    private ArrayList<Day> days;
    private static int minOffset = -5;

    public Subject getSubject() {
        return subject;
    }

    public Week getWeek() {
        return week;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    /**
     * This method returns the offset for a position, because schedules in the past are negative.
     * @param pos
     * @return
     */
    public static int getOffsetForPos(int pos) {
        return minOffset + pos;
    }

    public static int getPosForOffset(int offset) {
        return offset - minOffset;
    }
}
