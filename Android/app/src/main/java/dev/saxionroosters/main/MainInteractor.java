package dev.saxionroosters.main;

import org.greenrobot.eventbus.EventBus;

import dev.saxionroosters.Dataset;
import dev.saxionroosters.ScheduleRepository;
import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.general.ErrorUtils;
import dev.saxionroosters.eventbus.SearchResultEvent;
import dev.saxionroosters.general.ServiceGenerator;
import dev.saxionroosters.model.SearchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jelle on 29/11/2016.
 */

public class MainInteractor implements IMainInteractor {


    private ScheduleRepository repository;
    private Dataset dataset;

    public MainInteractor() {
        repository = ServiceGenerator.createService(ScheduleRepository.class);
        dataset = Dataset.getInstance();
    }

    @Override
    public void search(String query) {
        //check if we already have results for this query.
        SearchResult searchResult = dataset.getSearchResult(query);
        if(searchResult != null) {
            EventBus.getDefault().post(new SearchResultEvent(searchResult));
            return;
        }

        //else we try to request the results.
        final Call<SearchResult> result = repository.getSearchResultForQuery(query);
        result.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if(response.isSuccessful()) {
                    //save our search result.
                    dataset.addSearchResult(response.body());

                    //notify the presenter with the search results
                    EventBus.getDefault().post(new SearchResultEvent(response.body()));
                } else { //error handling
                    ErrorEvent errorEvent = ErrorUtils.parseError(response);
                    EventBus.getDefault().post(errorEvent);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                EventBus.getDefault().post(new ErrorEvent(-1, t.getMessage()));
            }
        });
    }
}
