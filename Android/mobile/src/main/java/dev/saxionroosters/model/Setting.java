package dev.saxionroosters.model;

import dev.saxionroosters.extras.S;

/**
 * Created by Doppie on 18-3-2016.
 */
public class Setting {

    private String title = S.UNKNOWN;
    private String subtitle = S.UNKNOWN;
    private boolean showSwitch = false;
    private String prefsId = S.UNKNOWN;

    public Setting(String title, String subtitle, boolean showSwitch, String prefsId) {
        this.title = title;
        this.subtitle = subtitle;
        this.showSwitch = showSwitch;
        this.prefsId = prefsId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isShowSwitch() {
        return showSwitch;
    }

    public String getPrefsId() {
        return prefsId;
    }
}
