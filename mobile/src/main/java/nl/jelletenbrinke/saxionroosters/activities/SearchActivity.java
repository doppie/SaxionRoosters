package nl.jelletenbrinke.saxionroosters.activities;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.extras.HtmlRetriever;
import nl.jelletenbrinke.saxionroosters.extras.S;

@EActivity(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity {

    @ViewById(R.id.searchView)
    protected SearchView searchView;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.list)
    protected RecyclerView list;

    private ProgressDialog dialog;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);

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
    }

    @UiThread
    protected void updateUI() {

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

}
