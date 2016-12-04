package dev.saxionroosters.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.general.ThemeUtils;
import dev.saxionroosters.searchdialog.SearchDialogFragment;

/**
 * Created by jelle on 01/12/2016.
 */

public class SettingsActivity extends AppCompatActivity implements SettingsView {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.settingsLayout)
    protected LinearLayout settingsLayout;
    @BindView(R.id.optionLayout)
    protected LinearLayout optionsLayout;

    private SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onCreateSetTheme(this, PreferenceManager.getInstance(this).read(Settings.THEME_COLOR));
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initUI();

        presenter = new SettingsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //ensure the backbutton is white on older devices aswell.
        Drawable backButton = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null);
        getSupportActionBar().setHomeAsUpIndicator(backButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void showSettings(ArrayList<Setting> settings) {
        settingsLayout.removeAllViewsInLayout();

        View settingsHeader = (View) getLayoutInflater().inflate(R.layout.list_item_settings_header, null);
        settingsLayout.addView(settingsHeader);

        for(final Setting setting : settings) {

            //init the view and its childs
            View settingView = (View) getLayoutInflater().inflate(R.layout.list_item_setting, null);
            TextView titleText = (TextView) settingView.findViewById(R.id.titleText);
            TextView subtitleText = (TextView) settingView.findViewById(R.id.subtitleText);
            Switch switchView = (Switch) settingView.findViewById(R.id.switchView);

            //Not every setting needs a switch.
            if(!setting.isSwitchEnabled()) {
                switchView.setVisibility(View.GONE);
            }

            //set the views values and listeners
            titleText.setText(setting.getTitle());
            subtitleText.setText(setting.getSubtitle());
            settingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.handleSettingsClick(setting);
                }
            });

            //add it to the layout.
            settingsLayout.addView(settingView);
        }
    }

    @Override
    public void showOptions(ArrayList<Option> options) {
        optionsLayout.removeAllViewsInLayout();

        View optionsHeader = (View) getLayoutInflater().inflate(R.layout.list_item_options_header, null);
        optionsLayout.addView(optionsHeader);

        for(final Option option : options) {

            //init the view and its childs
            View optionsView = (View) getLayoutInflater().inflate(R.layout.list_item_option, null);
            TextView titleText = (TextView) optionsView.findViewById(R.id.titleText);
            TextView subtitleText = (TextView) optionsView.findViewById(R.id.subtitleText);
            ImageView optionImage = (ImageView)  optionsView.findViewById(R.id.optionImage);

            //set the views values and listeners
            titleText.setText(option.getTitle());
            subtitleText.setText(option.getSubtitle());
            optionImage.setImageDrawable(option.getImage());
            optionsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.handleOptionsClick(option);
                }
            });

            //add it to the layout.
            optionsLayout.addView(optionsView);
        }
    }

    @Override
    public void showSelectDefaultGroupDialog() {
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("dialog", true);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void showSelectColorThemeDialog() {
        final String[] colorThemes = getResources().getStringArray(R.array.colorThemes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.title_select_color_theme));
        builder.setItems(colorThemes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                PreferenceManager.getInstance(getContext()).write(Settings.THEME_COLOR, colorThemes[pos]);
                ThemeUtils.activateTheme(SettingsActivity.this);
            }
        });
        builder.show();
    }

    @Override
    public void showUpgradeView() {

    }

    @Override
    public void showFeedbackView() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("*/mail");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_option_feedback) + " " + getString(R.string.app_name));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_roosters)});
        startActivity(Intent.createChooser(i, getString(R.string.message_feedback)));
    }

    @Override
    public void showLicensesView() {

    }


    @Override
    public void showGithubView() {
        String url = "https://github.com/doppie/SaxionRoosters";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @Override
    public void showAboutView() {

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(settingsLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }



}
