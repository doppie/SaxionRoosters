package dev.saxionroosters.searchdialog;

import android.app.Activity;
import android.content.Context;

import com.lapism.searchview.SearchItem;

import java.util.ArrayList;

import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 30/11/2016.
 */

public interface SearchDialogView extends IView {

    void clearSearchInput();

    void showSearchResults(ArrayList<SearchItem> results, String query);

    void showMessage(String message);

    void closeKeyboard();

    void dismissEmptyMessage();

    void showEmptyMessage();

    void dismissLoadingLayout();

    void showLoadingLayout();

    void dismiss();

    Activity getActivity();
}
