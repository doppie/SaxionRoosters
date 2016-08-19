package dev.saxionroosters.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.ThemeTools;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.fragments.SetupFragment;
import dev.saxionroosters.fragments.SetupFragment_;
import dev.saxionroosters.model.Setting;

/**
 * Created by hugo on 12/03/16.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.settingsLayout)
    protected LinearLayout settingsLayout;

    @AfterViews
    protected void init() {
        ThemeTools.onCreateSetTheme(this, (String) storage.getObject(S.SETTING_THEME_COLOR));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setSubtitle(getString(R.string.settings));

        //ensure the backbutton is white on older devices aswell.
        Drawable backButton = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        backButton.setColorFilter(ContextCompat.getColor(this, R.color.cpb_white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backButton);

        fillSettingsLayout(getSettings());
    }

    @Override
    @UiThread
    public void updateUI() {
        Log.e("debug", "settings updateUI");
        fillSettingsLayout(getSettings());
    }

    private void fillSettingsLayout(ArrayList<Setting> settings) {
        settingsLayout.removeAllViewsInLayout();

        for(final Setting s : settings) {
            View settingView = (View) getLayoutInflater().inflate(R.layout.item_settings, null);
            TextView titleText = (TextView) settingView.findViewById(R.id.titleText);
            TextView subtitleText = (TextView) settingView.findViewById(R.id.subtitleText);
            Switch switchView = (Switch) settingView.findViewById(R.id.switch1);

            if(!s.isShowSwitch()) switchView.setVisibility(View.GONE);
            titleText.setText(s.getTitle());
            subtitleText.setText(s.getSubtitle());
            settingView.setClickable(true);
            settingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleSettingsClick(s.getPrefsId());
                }
            });

            settingsLayout.addView(settingView);
        }
    }

    private void handleSettingsClick(String prefsId) {
        if(prefsId.equals(S.SETTING_STARTUP_OWNER)) {
            Intent i = new Intent(SettingsActivity.this, SearchActivity.class);
            SetupFragment dialog = new SetupFragment_();
            Bundle args = new Bundle();
            args.putString(S.VIEW_TYPE, S.DIALOG);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "dialog");

        } else if(prefsId.equals(S.SETTING_THEME_COLOR)) {
            final String[] themeColors = getResources().getStringArray(R.array.theme_colors);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(getString(R.string.pick_theme));
            builder.setItems(themeColors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    storage.saveObject(S.SETTING_THEME_COLOR, themeColors[item]);
                    ThemeTools.activateTheme(SettingsActivity.this);
                }
            });
            builder.show();

        }
    }


    public ArrayList<Setting> getSettings() {
        ArrayList<Setting> settings = new ArrayList<>();
        settings.add(new Setting(getString(R.string.setting_startup_owner_title), (String) storage.getObject(S.SETTING_STARTUP_OWNER), false, S.SETTING_STARTUP_OWNER));
        settings.add(new Setting(getString(R.string.theme_color), (String) storage.getObject(S.SETTING_THEME_COLOR), false, S.SETTING_THEME_COLOR));
        return settings;
    }

    @Override
    public void onResume() {
        super.onResume();
        fillSettingsLayout(getSettings());
        Log.e("debug", "onResume");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Tools.isLollipop()) supportFinishAfterTransition();
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
