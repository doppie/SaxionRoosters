package dev.saxionroosters.main;

import com.lapism.searchview.SearchItem;

import java.util.ArrayList;

import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 29/11/2016.
 */

public interface MainView extends IView {



    void showSchedulePager(String group);

    void updateTabTitle(int pos, String title);

    /**
     * Update the dropdownlist connected to the SearchView with the testresults.
     * @param results
     */
    void showSearchResults(ArrayList<SearchItem> results, String query);

    void showInterstitialAd();

    void showBannerAd();

    /**
     * Show a snackbar message to the user.
     * @param message
     */
    void showMessage(String message);

    void finish();
}
