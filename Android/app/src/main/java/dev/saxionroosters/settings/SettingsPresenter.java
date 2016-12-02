package dev.saxionroosters.settings;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.graphics.drawable.VectorDrawableCompat;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;

/**
 * Created by jelle on 01/12/2016.
 */

public class SettingsPresenter implements ISettingsPresenter {

    private SettingsView view;
    private Context context;
    private PreferenceManager prefsManager;

    public SettingsPresenter(SettingsView view) {
        this.view = view;
        this.context = view.getContext();
        this.prefsManager = PreferenceManager.getInstance(context);
    }

    @Override
    public void resume() {
        loadSettings();
        loadOptions();
    }

    @Override
    public void pause() {

    }

    @Override
    public void loadSettings() {
        ArrayList<Setting> settings = new ArrayList<>();
        settings.add(new Setting(Settings.DEFAULT_GROUP, context.getString(R.string.title_setting_group_default), prefsManager.read(Settings.DEFAULT_GROUP), false));
        settings.add(new Setting(Settings.THEME, context.getString(R.string.title_setting_theme), prefsManager.read(Settings.THEME), false));
        view.showSettings(settings);
    }

    @Override
    public void loadOptions(){
        ArrayList<Option> options = new ArrayList<>();
        options.add(new Option(Options.UPGRADE,
                context.getString(R.string.title_option_upgrade),
                context.getString(R.string.subtitle_option_upgrade),
                VectorDrawableCompat.create(context.getResources(), R.drawable.ic_heart_outline_black_24dp, null)));
        options.add(new Option(Options.FEEDBACK,
                context.getString(R.string.title_option_feedback),
                context.getString(R.string.subtitle_option_feedback),
                VectorDrawableCompat.create(context.getResources(), R.drawable.ic_mail_black_24dp, null)));
        options.add(new Option(Options.LICENSES,
                context.getString(R.string.title_option_licenses),
                context.getString(R.string.subtitle_option_licenses),
                VectorDrawableCompat.create(context.getResources(), R.drawable.ic_book_open_black_24dp, null)));
        try {
            options.add(new Option(Options.ABOUT,
                    context.getString(R.string.title_option_about),
                    context.getString(R.string.subtitle_option_about) + " " + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName,
                    VectorDrawableCompat.create(context.getResources(), R.drawable.ic_info_outline_black_24dp, null)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        view.showOptions(options);
    }


    @Override
    public void handleSettingsClick(Setting setting) {

    }

    @Override
    public void handleOptionsClick(Option option) {

    }
}
