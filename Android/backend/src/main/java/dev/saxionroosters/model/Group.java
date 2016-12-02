package dev.saxionroosters.model;

/**
 * Created by jelle on 27/11/2016.
 */

public class Group {

    private String name;
    private String course_id;
    private String course_name;
    private String academy;

    public Group(String name, String course_id, String course_name, String academy) {
        this.name = name;
        this.course_id = course_id;
        this.course_name = course_name;
        this.academy = academy;
    }

    public String getName() {
        return name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getAcademy() {
        return academy;
    }
}
