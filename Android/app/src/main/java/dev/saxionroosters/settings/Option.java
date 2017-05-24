package dev.saxionroosters.settings;

import android.graphics.drawable.Drawable;

/**
 * Created by jelle on 02/12/2016.
 */

public class Option {

    private Options option;
    private String title;
    private String subtitle;
    private Drawable image;

    public Option(Options option, String title, String subtitle, Drawable image) {
        this.option = option;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;

    }

    public Options getOption() {
        return option;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }


    public Drawable getImage() {
        return image;
    }

}
