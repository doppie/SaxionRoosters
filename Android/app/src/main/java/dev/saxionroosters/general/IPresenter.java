package dev.saxionroosters.general;

/**
 * Created by jelle on 27/11/2016.
 *
 * Basic Presenter Interface with general methods.
 */
public interface IPresenter {

    /**
     * Should be called @onResume of the Activity/Fragment hosting the presenter.
     */
    void resume();

    /**
     * Should be called @onPause of the Activity/Fragment hosting the presenter.
     */
    void pause();
}
