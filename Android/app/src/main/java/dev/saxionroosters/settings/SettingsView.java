package dev.saxionroosters.settings;

import java.util.ArrayList;

import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 01/12/2016.
 */

public interface SettingsView extends IView {

    void showSettings(ArrayList<Setting> settings);

    void showOptions(ArrayList<Option> options);

    void showSelectDefaultGroupDialog();

    void showSelectColorThemeDialog();

    void showUpgradeView();

    void showFeedbackView();

    void showLicensesView();

    void showGithubView();

    void showAboutView();
}
