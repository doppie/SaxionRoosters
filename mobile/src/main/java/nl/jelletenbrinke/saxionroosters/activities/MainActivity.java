package nl.jelletenbrinke.saxionroosters.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
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
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.adapters.WeekPagerAdapter;
import nl.jelletenbrinke.saxionroosters.extras.HtmlRetriever;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.extras.Storage;
import nl.jelletenbrinke.saxionroosters.extras.Tools;
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;


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
        initUI();
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
        if(!storage.getCurrentWeeks().isEmpty()) {
            toolbar.setSubtitle(storage.getCurrentWeeks().get(0).getOwner().getName());
        } else {
            toolbar.setTitle(getString(R.string.app_name));
        }

        if(storage.getCurrentWeeks() != null) {
            pager.setAdapter(null);
            pagerAdapter = new WeekPagerAdapter(this, getSupportFragmentManager(), storage.getCurrentWeeks());
            pager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(pager);

            //get the current week:
            Week currentWeek = null;
            for(Week week : storage.getCurrentWeeks()) {
                if(week.getId().equals("0")) currentWeek = week;
            }
            //select the current week
            if(currentWeek != null) pager.setCurrentItem(pagerAdapter.getItemPosition(currentWeek));
        }

//        searchView.setAdapter(null);
        ArrayList<SearchItem> searchResults = new ArrayList<>();
        //Adding our results
        if(storage.getSearchResults() != null) {
            for(Result result : storage.getSearchResults()) {
                SearchItem item = null;
                if(result.getName() != null && !result.getName().isEmpty()) {
                    item = new SearchItem(result.getAbbrevation() + " (" + result.getName() + ")", R.drawable.magnify_grey);
                } else if(result.getAbbrevation() != null) {
                    item = new SearchItem(result.getAbbrevation(), R.drawable.magnify_grey);
                }

                if(item != null) searchResults.add(item);

            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_search) {
            searchView.show(true);
            return true;
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
