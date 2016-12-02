package dev.saxionroosters.main;

import android.content.Intent;

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

public class MainPresenter implements IMainPresenter {

    private MainView view;
    private SearchInteractor interactor;
    private PreferenceManager prefsManager;

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
    }

    @Override
    public void pause() {
        EventBus.getDefault().unregister(this);
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

    @Subscribe
    public void onSearchResults(SearchResultEvent event) {

        //create a list of results
        ArrayList<SearchItem> items = new ArrayList<>();

        for(Group group : event.getResult().getGroups()) {
            items.add(new SearchItem(group.getName()));
        }

        view.showSearchResults(items);

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
        Utils.log("[Error] " + event.getMessage());
        view.showMessage(event.getMessage());
    }

}