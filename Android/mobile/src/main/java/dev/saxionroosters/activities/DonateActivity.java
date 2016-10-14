package dev.saxionroosters.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.AnalyticsTrackers;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;

/**
 * Created by jelle on 09/10/16.
 */
@EActivity(R.layout.activity_donate)
public class DonateActivity extends BaseActivity {

    @ViewById(R.id.denyButton)
    protected Button denyButton;

    @ViewById(R.id.donateButton)
    protected Button donateButton;

    @ViewById(R.id.closeButton)
    protected ImageView closeButton;

    @ViewById(R.id.text)
    protected TextView text;

    @AfterViews
    protected void init() {
        initUI();
        updateUI();
    }

    private void initUI() {
        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsTrackers.sendEvent(S.DONATE, denyButton.getText().toString());
                finish();
            }
        });
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsTrackers.sendEvent(S.DONATE, "purchase_started");
                billingProcessor.purchase(DonateActivity.this, "premium");
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsTrackers.sendEvent(S.DONATE, "closebutton_" + denyButton.getText().toString());
                finish();
            }
        });
    }

    @Override
    @UiThread
    public void updateUI() {
        if (Boolean.valueOf(storage.getObject("premium"))) {
            text.setText(getString(R.string.donate_thankyou));
            donateButton.setVisibility(View.GONE);
            denyButton.setText(getString(R.string.close));
        } else {
            denyButton.setText(getRandomText());
        }

    }

    private String getRandomText() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(getString(R.string.donate_cancel_1));
        strings.add(getString(R.string.donate_cancel_2));
        strings.add(getString(R.string.donate_cancel_3));
        strings.add(getString(R.string.donate_cancel_4));
        strings.add(getString(R.string.donate_cancel_5));
        strings.add(getString(R.string.donate_cancel_6));

        Random rand = new Random();
        return strings.get(rand.nextInt((5 - 0) + 1) + 0);
    }


    public static final int LAUNCHES_UNTIL_PROMPT = 3;
    public static final int DAYS_UNTIL_PROMPT = 2;

    public static void app_launched(BaseActivity a) {
        SharedPreferences prefs = a.getSharedPreferences("donate_activity", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        if (launch_count >= (LAUNCHES_UNTIL_PROMPT)) {
            editor.putBoolean("showads", true);
        } else {
            editor.putBoolean("showads", false);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {

                editor.putBoolean("dontshowagain", true);
                editor.commit();
                AnalyticsTrackers.sendEvent(S.DONATE, "show_auto");
                Intent i = new Intent(a, DonateActivity_.class);
                a.startActivity(i);

            }
        }

        editor.commit();
    }

    public static boolean showAds(BaseActivity a) {
        SharedPreferences prefs = a.getSharedPreferences("donate_activity", 0);
        int adscounter = prefs.getInt("adscounter", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("adscounter", adscounter+1);
        editor.apply();
        if(adscounter % 2 == 0) {
            return false;
        }
        return prefs.getBoolean("showads", false);
    }
}
