package dev.saxionroosters.searchdialog;

import android.content.Intent;

import com.lapism.searchview.SearchItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.eventbus.RefreshEvent;
import dev.saxionroosters.eventbus.SearchResultEvent;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.settings.Settings;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.main.MainActivity;
import dev.saxionroosters.main.SearchInteractor;
import dev.saxionroosters.model.Group;

/**
 * Created by jelle on 30/11/2016.
 */

public class SearchDialogPresenter implements ISearchDialogPresenter {

    private SearchDialogView view;
    private SearchInteractor interactor;
    private PreferenceManager prefsManager;
    private boolean isDialog;

    public SearchDialogPresenter(SearchDialogView view, boolean isDialog) {
        this.view = view;
        this.interactor = new SearchInteractor();
        this.isDialog = isDialog;
        this.prefsManager = PreferenceManager.getInstance(view.getContext());
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
        view.showLoadingLayout();
        interactor.search(query);
    }

    @Override
    public void finishSelection(SearchItem item) {

        //save this as our default schedule to our preferences.
        prefsManager.write(Settings.DEFAULT_GROUP, item.get_text().toString());
        if(!isDialog) {
            Intent i = new Intent(view.getActivity(), MainActivity.class);
            view.getContext().startActivity(i);
            view.getActivity().finish();
        } else {
            view.dismiss();
            EventBus.getDefault().post(new RefreshEvent());
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

        if (items.size() > 0) {
            view.dismissEmptyMessage();
        } else {
            view.showEmptyMessage();
        }

        view.dismissLoadingLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorReceived(ErrorEvent event) {
        Utils.log("[Error] " + event.getMessage());
        view.showMessage(event.getMessage());
    }

}
