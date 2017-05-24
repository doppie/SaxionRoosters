package dev.saxionroosters.settings;

import android.content.Intent;

import dev.saxionroosters.general.IPresenter;

/**
 * Created by jelle on 01/12/2016.
 */

public interface ISettingsPresenter extends IPresenter {

    void handleSettingsClick(Setting setting);

    void handleOptionsClick(Option option);

    void loadSettings();

    void loadOptions();

    void initBillingProcessor();

    boolean handleActivityResult(int requestCode, int resultCode, Intent intent);
}
