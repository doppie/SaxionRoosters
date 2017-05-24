package dev.saxionroosters.general;

import android.content.Context;

/**
 * Created by jelle on 28/11/2016.
 *
 * Basic View Interface with general methods.
 */
public interface IView {

    /**
     * This method should be used to ensure that the UI has the correct state when initialised.
     */
    void initUI();

    Context getContext();
}
