package dev.saxionroosters.introduction;

/**
 * Created by jelle on 30/11/2016.
 */

public class IntroductionPresenter implements IIntroductionPresenter {

    IntroductionView view;

    public IntroductionPresenter(IntroductionView view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
