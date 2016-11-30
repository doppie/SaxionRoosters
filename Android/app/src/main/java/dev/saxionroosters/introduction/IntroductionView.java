package dev.saxionroosters.introduction;

import android.support.v4.app.Fragment;

import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 30/11/2016.
 */

public interface IntroductionView extends IView {


    /**
     * Scrolls to the fragment.
     * @param fragment
     */
    void moveToFragment(Fragment fragment);


    void showMessage(String message);

    void finish();

}
