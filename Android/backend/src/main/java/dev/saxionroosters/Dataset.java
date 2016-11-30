package dev.saxionroosters;

import java.util.ArrayList;

import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.model.SearchResult;

/**
 * Created by jelle on 29/11/2016.
 */

public class Dataset {

    private ArrayList<Schedule> schedules;
    private ArrayList<SearchResult> results;

    private static Dataset instance;

    public static Dataset getInstance() {
        if(instance == null) {
            instance = new Dataset();
        }
        return instance;
    }

    public Dataset() {
        this.schedules = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    /**
     * Returns a clone, can only be used to read data, not to adjust.
     * @return
     */
    public ArrayList<Schedule> getSchedules() {
        return (ArrayList<Schedule>) schedules.clone();
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    /**
     * Searches for a specific schedule that might already be loaded in this session.
     * @param group
     * @param offset the week offset from this week
     * @return found ? schedule : null
     */
    public Schedule getSchedule(String group, String offset) {
        for(Schedule schedule : schedules) {
            if(schedule.getSubject().getGroup().getName().equalsIgnoreCase(group)
                    && schedule.getWeek().getOffset().equals(offset)) {
                return schedule;
            }
        }

        return null;
    }


    public void addSearchResult(SearchResult result) {
        results.add(result);
    }

    /**
     * Checks if we already have results for this specific query.
     * @param query
     * @return found ? result : null
     */
    public SearchResult getSearchResult(String query) {
        for(SearchResult result : results) {
            if(result.getQuery().equalsIgnoreCase(query)) {
                return result;
            }
        }
        return null;
    }
}
