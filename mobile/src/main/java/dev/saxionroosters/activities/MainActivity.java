package dev.saxionroosters.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.lapism.searchview.adapter.SearchAdapter;
import com.lapism.searchview.adapter.SearchItem;
import com.lapism.searchview.view.SearchCodes;
import com.lapism.searchview.view.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.WeekPagerAdapter;
import dev.saxionroosters.dialogs.AboutDialog;
import dev.saxionroosters.dialogs.FeedbackDialog;
import dev.saxionroosters.dialogs.RateDialog;
import dev.saxionroosters.extras.AnalyticsTrackers;
import dev.saxionroosters.extras.HtmlRetriever;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Week;


/**
 * The main activity.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    //UI
    @ViewById(R.id.mainLayout)
    protected CoordinatorLayout mainLayout;

    @ViewById(R.id.container)
    protected ViewPager pager;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.searchView)
    protected SearchView searchView;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    //adapters
    private WeekPagerAdapter pagerAdapter;
    private SearchAdapter searchAdapter;


    @AfterViews
    protected void init() {

        AnalyticsTrackers.initialize(this);
        new FeedbackDialog().app_launched(this);
        new RateDialog().app_launched(this);
        initUI();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: only call when you want to show update intro or first time intro.
        //Can only be called in oncreate or later, @afterViews is too early and will create errors.
        Intent i = new Intent(MainActivity.this, IntroActivity_.class);
        startActivity(i);
    }

    /* Initializes the UI, called from @onCreate */
    private void initUI() {
        setSupportActionBar(toolbar);


        //Don't load all pages at once, only load one extra left and right of the current view.
        pager.setOffscreenPageLimit(1);

//        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setVersion(SearchCodes.VERSION_MENU_ITEM);
        searchView.setStyle(SearchCodes.STYLE_MENU_ITEM_CLASSIC);
        searchView.setTheme(SearchCodes.THEME_LIGHT);
        searchView.setAnimationDuration(300);
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

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        Log.e("debug", "updateUI");

        //Update the titlebar
        if(!storage.getCurrentWeeks().isEmpty()) toolbar.setSubtitle(Tools.getOwnerRepresentativeName(storage.getCurrentWeeks().get(0).getOwner()));

        //Check if the pager needs an update.
        if(storage.getCurrentWeeks() != null) {
            if(pagerAdapter != null && pagerAdapter.getCount() > 0 && !storage.getCurrentWeeks().isEmpty()) {
                Owner oldOwner = pagerAdapter.getOwnerForWeeks();
                Owner newOwner = storage.getCurrentWeeks().get(0).getOwner();
                if(!oldOwner.equals(newOwner) && oldOwner != null) updatePager();
            } else {
                updatePager();
            }
        }

        //Update the searchresults in the searchview.
        ArrayList<SearchItem> searchResults = new ArrayList<>();
        if(storage.getSearchResults() != null) searchResults = Tools.getResultsForSearchView(storage.getSearchResults());
        searchAdapter = new SearchAdapter(this, new ArrayList<SearchItem>(), searchResults, SearchCodes.THEME_LIGHT);
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                searchView.hide(true);

                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String text = textView.getText() + "";
                text = Tools.parseQueryFromName(text);

                getWeekPager(text.toString());
            }
        });
        searchView.setAdapter(searchAdapter);
    }

    private void updatePager() {
        pager.setAdapter(null);
        pagerAdapter = new WeekPagerAdapter(this, getSupportFragmentManager(), storage.getCurrentWeeks());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

        //get the current week:
        Week currentWeek = null;
        for (Week week : storage.getCurrentWeeks()) {
            if (week.getId().equals("0")) currentWeek = week;
        }
        //select the current week
        if (currentWeek != null) pager.setCurrentItem(pagerAdapter.getItemPosition(currentWeek));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_search) {
            searchView.show(true);
            return true;
        } else if(id == R.id.action_info) {
            AboutDialog dialog = new AboutDialog();
            dialog.show(getSupportFragmentManager(), "dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public ViewPager getPager() {
        return pager;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Background
    protected void getWeekPager(String query) {
        preExecute();

        HtmlRetriever retriever = new HtmlRetriever(this);
        String url = S.URL + S.QUERY + query;
        Object object = retriever.retrieveHtml(url, S.PARSE_WEEK_PAGER, query);

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
