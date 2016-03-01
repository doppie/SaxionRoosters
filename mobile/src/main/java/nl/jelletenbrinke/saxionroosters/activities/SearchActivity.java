package nl.jelletenbrinke.saxionroosters.activities;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.adapters.OwnerAdapter;
import nl.jelletenbrinke.saxionroosters.extras.HtmlRetriever;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.extras.Storage;
import nl.jelletenbrinke.saxionroosters.interfaces.ClickListener;
import nl.jelletenbrinke.saxionroosters.model.Owner;
import nl.jelletenbrinke.saxionroosters.model.Result;

@EActivity(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements ClickListener{

    @ViewById(R.id.searchView)
    protected SearchView searchView;

    @ViewById(R.id.list)
    protected RecyclerView list;

    @ViewById(R.id.noResultsView)
    protected TextView noResultsView;

    //adapters
    private OwnerAdapter ownerAdapter;
    private RecyclerView.LayoutManager listLayoutManager;


    @AfterViews
    protected void init() {
        String searchQuery = getIntent().getStringExtra(S.SEARCH_QUERY);
        if(searchQuery != null && searchQuery.isEmpty()) searchView.setQuery(searchQuery);

        searchView.setVersion(SearchCodes.VERSION_TOOLBAR);
        searchView.setStyle(SearchCodes.STYLE_TOOLBAR_CLASSIC);
        searchView.setTheme(SearchCodes.THEME_LIGHT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getWeekPager(query);
                searchView.hide(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) getSearchResults(newText);
                return false;
            }
        });

        //fixed size always true: important for performance!
        list.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(listLayoutManager);

        //This one is used whenever an item is removed or added to the adapter
        //It animates the item nicely :)
        list.setItemAnimator(new DefaultItemAnimator());

        updateUI();
    }

    @UiThread
    protected void updateUI() {
        ArrayList<Owner> searchResults = new ArrayList<>();
        //Adding our results
        if(storage.getSearchResults() != null) {
            for(Result result : storage.getSearchResults()) {
                Owner owner = null;
                Owner.OwnerType type = Owner.OwnerType.GROUP;
                if(result.getType() != null && result.getType().equals(S.TEACHER)) type = Owner.OwnerType.TEACHER;
                if(result.getName() != null && !result.getName().isEmpty()) {

                    owner = new Owner(result.getAbbrevation() + " (" + result.getName() + ")", type);
                } else if(result.getAbbrevation() != null) {
                    owner = new Owner(result.getAbbrevation(), type);
                }

                if(owner != null) searchResults.add(owner);

            }
        }
        ownerAdapter = new OwnerAdapter(searchResults, this);
        list.setAdapter(ownerAdapter);

        if(ownerAdapter.getItemCount() == 0) {
            noResultsView.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        } else {
            noResultsView.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
    }

    @Background
    protected void getWeekPager(String name) {
        preExecute();

        HtmlRetriever retriever = new HtmlRetriever(this);
        String url = S.URL + S.QUERY + name;
        Object object = retriever.retrieveHtml(url, S.PARSE_WEEK_PAGER);

        postExecute(retriever, object);
    }

    @Background
    protected void getSearchResults(String query) {
        HtmlRetriever retriever = new HtmlRetriever(this);
        String url = S.URL + S.QUERY + query;
        Object object = retriever.retrieveHtml(url, S.PARSE_SEARCH_RESULTS, query);

        postExecute(retriever, object);
    }

    @UiThread
    protected void postExecute(HtmlRetriever retriever, Object object) {
        retriever.onWeekPagerRetrieveCompleted(object);
        updateUI();
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @UiThread
    protected void preExecute() {
        dialog = new ProgressDialog(this);
        this.dialog.setMessage(getString(R.string.loading));
        this.dialog.show();
    }

    @Override
    public void onClick(int position, boolean isLongClick) {
        //TODO: handle on item click from list.
    }
}
