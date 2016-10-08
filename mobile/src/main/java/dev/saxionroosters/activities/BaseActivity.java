package dev.saxionroosters.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import dev.saxionroosters.NotificationPublisher;
import dev.saxionroosters.R;
import dev.saxionroosters.extras.AnalyticsTrackers;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Storage;
import dev.saxionroosters.extras.ThemeTools;

/**
 * Created by Doppie on 1-3-2016.
 */
@EActivity
public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog dialog;
    protected InterstitialAd interstitialAd;

    @Bean
    protected Storage storage;

    @UiThread
    public void updateUI() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeTools.onCreateSetTheme(this, (String) storage.getObject(S.SETTING_THEME_COLOR));

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
    }

    public void startIssueReporter() {
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    protected void initInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                requestNewInterstitialAd();
            }
        });

        requestNewInterstitialAd();
    }

    protected void requestNewInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        interstitialAd.loadAd(adRequest);
    }

    protected boolean showInterstitialAd() {
        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
            return true;
        }

        return false;
    }

//    public void scheduleNotification(Notification notification, int delay) {
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long futureInMillis = SystemClock.elapsedRealtime() + delay;
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
//        Toast.makeText(this, "Notification scheduled.", Toast.LENGTH_SHORT).show();
//    }

//    public Notification getNotification(String content) {
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.drawable.app_icon);
//        return builder.build();
//    }

}
