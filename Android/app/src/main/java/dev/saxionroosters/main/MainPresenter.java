package dev.saxionroosters.main;

import com.lapism.searchview.SearchItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import dev.saxionroosters.eventbus.SearchResultEvent;
import dev.saxionroosters.model.Group;

/**
 * Created by jelle on 29/11/2016.
 */

public class MainPresenter implements IMainPresenter {


    private MainView view;
    private MainInteractor interactor;

    public MainPresenter(MainView view) {
        this.view = view;
        this.interactor = new MainInteractor();
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
    }

    @Subscribe
    public void onSearchResults(SearchResultEvent event) {

        //create a list of results
        ArrayList<SearchItem> items = new ArrayList<>();

        for(Group group : event.getResult().getGroups()) {
            items.add(new SearchItem(group.getName()));
        }

        view.showSearchResults(items);
    }
}
