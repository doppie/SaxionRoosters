package dev.saxionroosters.introduction;

import android.content.Intent;

import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.settings.Settings;
import dev.saxionroosters.main.MainActivity;

/**
 * Created by jelle on 30/11/2016.
 */

public class IntroductionPresenter implements IIntroductionPresenter {

    private IntroductionView view;
    private PreferenceManager prefsManager;

    public IntroductionPresenter(IntroductionView view) {
        this.view = view;
        this.prefsManager = PreferenceManager.getInstance(view.getContext());
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void skipIntroduction() {
        if(prefsManager.read(Settings.DEFAULT_GROUP).isEmpty()) {
            view.showMessage(view.getContext().getString(R.string.error_no_default_group));
            view.moveToLastFragment();
        } else {
            Intent i = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(i);
            view.finish();
        }
    }
}
