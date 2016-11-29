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

    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() {
        return groups;
    }
}
