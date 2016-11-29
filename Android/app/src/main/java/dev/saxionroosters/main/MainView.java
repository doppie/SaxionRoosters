package dev.saxionroosters.main;

import com.lapism.searchview.SearchItem;

import java.util.ArrayList;

import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 29/11/2016.
 */

public interface MainView extends IView {



    void showSchedulePager(String group);

    void showSearchResults(ArrayList<SearchItem> results);

    void showInterstitialAd();

    void showBannerAd();
}
