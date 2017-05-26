package dev.saxionroosters.searchdialog;

import com.lapism.searchview.SearchItem;

import dev.saxionroosters.general.IPresenter;

/**
 * Created by jelle on 30/11/2016.
 */

public interface ISearchDialogPresenter extends IPresenter {

    void search(String query);

    void finishSelection(SearchItem item);

}
