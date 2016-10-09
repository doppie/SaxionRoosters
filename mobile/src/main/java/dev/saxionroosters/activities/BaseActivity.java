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

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
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
import dev.saxionroosters.extras.Tools;

/**
 * Created by Doppie on 1-3-2016.
 */
@EActivity
public class BaseActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    protected ProgressDialog dialog;
    protected InterstitialAd interstitialAd;
    protected BillingProcessor billingProcessor;

    @Bean
    protected Storage storage;

    @UiThread
    public void updateUI() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeTools.onCreateSetTheme(this, (String) storage.getObject(S.SETTING_THEME_COLOR));

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        initInAppBilling();
    }


    private void initInAppBilling() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if(isAvailable) billingProcessor = new BillingProcessor(this, getString(R.string.iab_id), this);
    }


    public void startIssueReporter() {
        Intent i = new Intent(this, FeedbackActivity.class);
        startActivity(i);
    }

    protected void initInterstitialAd() {
        if(Boolean.valueOf(storage.getObject("premium"))) return;
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
        if(Boolean.valueOf(storage.getObject("premium"))) return;
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("96CC90CB12D776D19FA21597DD1A2202")
                .build();
        interstitialAd.loadAd(adRequest);
    }

    protected boolean showInterstitialAd() {
        if(Boolean.valueOf(storage.getObject("premium"))) return false;
        if(interstitialAd.isLoaded()) {
            interstitialAd.show();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null)
            billingProcessor.release();

        super.onDestroy();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        Tools.log("Purchased: " + productId + " Details: " + details.toString());
        storage.saveObject("premium", true);
        updateUI();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Tools.log("Purchase history restored");

        storage.saveObject("premium", billingProcessor.isPurchased("premium"));
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
        updateUI();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Tools.log("Billing error: " + errorCode);
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
        updateUI();
    }

    @Override
    public void onBillingInitialized() {
        Tools.log("Billing initialized, ready to purchase");
        billingProcessor.loadOwnedPurchasesFromGoogle();
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (billingProcessor == null || !billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
