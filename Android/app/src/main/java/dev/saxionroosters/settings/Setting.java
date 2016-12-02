package dev.saxionroosters.settings;

/**
 * Created by jelle on 02/12/2016.
 */

public class Setting {

    private Settings settingId;
    private String title;
    private String subtitle;
    private boolean switchEnabled;

    public Setting(Settings settingId, String title, String subtitle, boolean switchEnabled) {
        this.settingId = settingId;
        this.title = title;
        this.subtitle = subtitle;
        this.switchEnabled = switchEnabled;
    }

    public Settings getSettingId() {
        return settingId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isSwitchEnabled() {
        return switchEnabled;
    }
}
