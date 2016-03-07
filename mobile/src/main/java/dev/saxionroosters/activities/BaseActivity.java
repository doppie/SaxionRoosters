package dev.saxionroosters.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.AnalyticsTrackers;
import dev.saxionroosters.extras.Storage;

/**
 * Created by Doppie on 1-3-2016.
 */
@EActivity(R.layout.activity_main)
public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog dialog;

    @Bean
    protected Storage storage;

}
