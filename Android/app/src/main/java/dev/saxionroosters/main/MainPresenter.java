package dev.saxionroosters.main;

import android.content.Intent;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.lapism.searchview.SearchItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.eventbus.ScheduleEvent;
import dev.saxionroosters.eventbus.SearchResultEvent;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.settings.Settings;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.introduction.IntroductionActivity;
import dev.saxionroosters.model.Group;
import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.settings.SettingsActivity;

/**
 * Created by jelle on 29/11/2016.
 */

public class MainPresenter implements IMainPresenter, BillingProcessor.IBillingHandler {

    private MainView view;
    private SearchInteractor interactor;
    private PreferenceManager prefsManager;
    private BillingProcessor billingProcessor;

    public MainPresenter(MainView view) {
        this.view = view;
        this.interactor = new SearchInteractor();
        this.prefsManager = PreferenceManager.getInstance(view.getContext());

        if(prefsManager.read(Settings.DEFAULT_GROUP).isEmpty()) {
            startIntroduction();
        } else {
            view.showSchedulePager(prefsManager.read(Settings.DEFAULT_GROUP));
        }
    }

    @Override
    public void resume() {
        EventBus.getDefault().register(this);
        initBillingProcessor();
    }

    @Override
    public void pause() {
        EventBus.getDefault().unregister(this);
        if (billingProcessor != null) billingProcessor.release();
    }

    @Override
    public void search(String query) {
        interactor.search(query);

        //TODO: indicate that we are loading search results.
    }

    @Override
    public void startIntroduction() {
        Intent i = new Intent(view.getContext(), IntroductionActivity.class);
        view.getContext().startActivity(i);
        view.finish();
    }

    @Override
    public void showSettings() {
        Intent i = new Intent(view.getContext(), SettingsActivity.class);
        view.getContext().startActivity(i);
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

    @Subscribe
    public void onSearchResults(SearchResultEvent event) {

        //create a list of results
        ArrayList<SearchItem> items = new ArrayList<>();

        for (Group group : event.getResult().getGroups()) {
            items.add(new SearchItem(group.getName()));
        }

        view.showSearchResults(items, event.getResult().getQuery());

        //TODO: stop indicating that we are loading search results.
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewScheduleLoaded(ScheduleEvent event) {
        int pos = Schedule.getPosForOffset(event.getOffset());
        String title = "..";
        if(pos == 4) {
            title = view.getContext().getString(R.string.week_current);
        } else if(event.getSchedule() != null){
            title = event.getSchedule().getWeek().getQuartile_week();
        }
        view.updateTabTitle(pos, title);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorReceived(ErrorEvent event) {
        Utils.log("[Error] " + event.getMessage() + " code: " + event.getStatus());
        view.showMessage(event.getMessage());
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        prefsManager.write(Settings.UPGRADED, true + "");
        view.showMessage(view.getContext().getString(R.string.message_purchase_success));
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