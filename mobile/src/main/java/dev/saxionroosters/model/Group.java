package dev.saxionroosters.model;

/**
 * Created by Doppie on 22-2-2016.
 */
public class Group extends Owner {

    private String courseName;

    public Group(String name, String courseName) {
        super(name, OwnerType.GROUP);
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }
}
