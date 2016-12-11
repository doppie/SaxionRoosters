package dev.saxionroosters.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.eventbus.RefreshEvent;
import dev.saxionroosters.general.PreferenceManager;

/**
 * Created by jelle on 01/12/2016.
 */

public class SettingsPresenter implements ISettingsPresenter, BillingProcessor.IBillingHandler {

    private SettingsView view;
    private Context context;
    private PreferenceManager prefsManager;
    private BillingProcessor billingProcessor;

    public SettingsPresenter(SettingsView view) {
        this.view = view;
        this.context = view.getContext();
        this.prefsManager = PreferenceManager.getInstance(context);
    }

    @Override
    public void resume() {
        initBillingProcessor();
        EventBus.getDefault().register(this);
        loadSettings();
        loadOptions();
    }

    @Override
    public void pause() {
        if(billingProcessor != null) billingProcessor.release();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadSettings() {
        ArrayList<Setting> settings = new ArrayList<>();
        settings.add(new Setting(Settings.DEFAULT_GROUP, context.getString(R.string.title_setting_group_default), prefsManager.read(Settings.DEFAULT_GROUP), false));
        settings.add(new Setting(Settings.THEME_COLOR, context.getString(R.string.title_setting_theme), prefsManager.read(Settings.THEME_COLOR), false));
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
        options.add(new Option(Options.GITHUB,
                context.getString(R.string.title_option_github),
                context.getString(R.string.subtitle_option_github),
                VectorDrawableCompat.create(context.getResources(), R.drawable.github_circle, null)));

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
    public void initBillingProcessor() {
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(view.getContext());
        if(isAvailable) billingProcessor = new BillingProcessor(view.getContext(), view.getContext().getString(R.string.iab_id), this);
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent intent) {
        if(billingProcessor == null || billingProcessor.handleActivityResult(requestCode, resultCode, intent)) {
            return false;
        } else {
            return true;
        }
    }



    @Override
    public void handleSettingsClick(Setting setting) {
        switch(setting.getSettingId()) {
            case DEFAULT_GROUP:
                view.showSelectDefaultGroupDialog();
                break;
            case THEME_COLOR:
                view.showSelectColorThemeDialog();
                break;

        }
    }

    @Override
    public void handleOptionsClick(Option option) {
        switch (option.getOption()) {
            case UPGRADE:
                if(billingProcessor != null) {
                    billingProcessor.purchase(view.getActivity(), "premium");
                } else {
                    view.showMessage(context.getString(R.string.error_no_iab_available));
                }
                break;
            case FEEDBACK:
                view.showFeedbackView();
                break;
            case LICENSES:
                view.showLicensesView();
                break;
            case GITHUB:
                view.showGithubView();
                view.showMessage(context.getString(R.string.message_redirect_github));
                break;
            case ABOUT:
                view.showAboutView();
                break;
        }
    }

    @Subscribe
    public void onRefreshEvent(RefreshEvent event) {
        loadSettings();
        loadOptions();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        prefsManager.write(Settings.UPGRADED, true + "");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        prefsManager.write(Settings.UPGRADED, billingProcessor.isPurchased("premium") + "");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
    }

    @Override
    public void onBillingInitialized() {
        billingProcessor.loadOwnedPurchasesFromGoogle();
    }
}
