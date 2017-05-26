package dev.saxionroosters.eventbus;

import dev.saxionroosters.model.SearchResult;

/**
 * Created by jelle on 29/11/2016.
 */

public class SearchResultEvent {

    private SearchResult result;

    public SearchResultEvent(SearchResult result) {
        this.result = result;
    }

    public SearchResult getResult() {
        return result;
    }
}
