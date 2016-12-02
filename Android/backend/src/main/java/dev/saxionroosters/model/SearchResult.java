package dev.saxionroosters.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jelle on 29/11/2016.
 */
public class SearchResult {

    //TODO: For now we just support groups, but it is easy to also add:
    //courses
    //students
    //teachers
    //academies

    private String query;
    private ArrayList<Group> groups;

    public String getQuery() {
        return query;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
}
