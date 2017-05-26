package dev.saxionroosters.main;

import android.content.Intent;

import dev.saxionroosters.general.IPresenter;

/**
 * Created by jelle on 29/11/2016.
 */

public interface IMainPresenter extends IPresenter {

    void search(String query);

    void startIntroduction();

    void showSettings();

    void initBillingProcessor();

    boolean handleActivityResult(int requestCode, int resultCode, Intent intent);
}
