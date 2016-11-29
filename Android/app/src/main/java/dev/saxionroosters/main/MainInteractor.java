package dev.saxionroosters.main;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import dev.saxionroosters.Dataset;
import dev.saxionroosters.ScheduleRepository;
import dev.saxionroosters.eventbus.SearchResultEvent;
import dev.saxionroosters.general.Tools;
import dev.saxionroosters.model.SearchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jelle on 29/11/2016.
 */

public class MainInteractor implements IMainInteractor {

    private Retrofit retrofit;
    private ScheduleRepository repository;
    private Dataset dataset;
    private Gson gson;

    public MainInteractor() {
        gson = Tools.getGson();
        //init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.roosters.saxion.nl/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        repository = retrofit.create(ScheduleRepository.class);

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

                //notify the presenter with the search results
                EventBus.getDefault().post(new SearchResultEvent(response.body()));
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Tools.log("Failure: " + t.toString());
            }
        });
    }
}
