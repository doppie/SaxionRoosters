package dev.saxionroosters.model;

import java.util.Date;

/**
 * Created by Doppie on 31-3-2016.
 */
public class RoosterNotification {

    private int id;
    private String title;
    private String subtitle;
    private Date showDate;

    public RoosterNotification(int id, String title, String subtitle, Date showDate) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.showDate = showDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Date getShowDate() {
        return showDate;
    }
}
