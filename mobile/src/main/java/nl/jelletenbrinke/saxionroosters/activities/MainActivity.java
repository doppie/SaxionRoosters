package nl.jelletenbrinke.saxionroosters.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.adapters.WeekPagerAdapter;
import nl.jelletenbrinke.saxionroosters.dialogs.ErrorDialog;
import nl.jelletenbrinke.saxionroosters.extras.NetworkAsyncTask;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.interfaces.OnAsyncTaskCompleted;
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;


/**
 * The main activity.
 */
public class MainActivity extends AppCompatActivity implements OnAsyncTaskCompleted {

    //UI
    private CoordinatorLayout mainLayout;
    private ViewPager pager;
    private TabLayout tabLayout;
    private SearchBox search;
    private Toolbar toolbar;

    //adapters
    private WeekPagerAdapter pagerAdapter;

    //data
    private String group = "";
    private ArrayList<Week> weeks;
    private ArrayList<Result> searchResults;

    private NetworkAsyncTask getSearchResultsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        getWeekPager(group);

        initUI();
    }

    /* Initializes the UI, called from @onCreate */
    private void initUI() {
        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
//        search.setMenuVisibility(View.GONE);

        pager = (ViewPager) findViewById(R.id.container);
        pager.setOffscreenPageLimit(1);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        if(weeks != null) {
            pager.setAdapter(null);
            pagerAdapter = new WeekPagerAdapter(getSupportFragmentManager(), weeks, group);
            pager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(pager);
        }
        search.clearSearchable();
        //Adding our results
        if(searchResults != null) {
            for(Result result : searchResults) {
                SearchResult option = new SearchResult(result.getAbbrevation() + " (" + result.getName() + ")", getResources().getDrawable(R.drawable.magnify_grey));
                search.addSearchable(option);

            }
        }
        search.updateResults();
    }

    private void getWeekPager(String group) {
        this.group = group;
        NetworkAsyncTask getWeekPagerTask = new NetworkAsyncTask(this, this, true);
        String url = S.URL + S.QUERY + group;
        getWeekPagerTask.execute(url, S.PARSE_WEEK_PAGER);
    }

    private void getSearchResults(String query) {
        //First cancel if necessary.
        if(getSearchResultsTask != null) getSearchResultsTask.cancel(true);

        getSearchResultsTask = new NetworkAsyncTask(this, this, false);
        String url = S.URL + S.QUERY + query;
        getSearchResultsTask.execute(url, S.PARSE_SEARCH_RESULTS, query);
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
            openSearch();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncTaskCompleted(Object obj) {

        if(obj == null) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        } else if(obj instanceof ArrayList) {
            ArrayList<Object> arrayList = (ArrayList<Object>) obj;

            //an empty arraylist this means the result is bad.
            if(arrayList.isEmpty()) {
                return;
            }

            if(arrayList.get(0) instanceof Week) {
                //We received the empty weeks for the pager!
                ArrayList<Week> newWeeks = (ArrayList<Week>) obj;
                this.weeks = newWeeks;
            } else if(arrayList.get(0) instanceof Result) {
                ArrayList<Result> results = (ArrayList<Result>) obj;
                //for now remove all courses, because we cannot handle them yet.
                for(int i = 0; i < results.size(); i++) {
                    Result r = results.get(i);
                    if(r.getType() == null || r.getType().equals(S.COURSE)) {
                        results.remove(r);
                    }
                }
                this.searchResults = results;
            }
        } else if(obj instanceof Exception) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(S.MESSAGE, getString(R.string.error_message_no_internet));
            args.putString(S.TITLE, getString(R.string.error_title_no_internet));
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "dialog");
        }


        updateUI();
    }

    public void openSearch() {
//        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);


        //First add the search options from history.
//        SearchResult option1 = new SearchResult("EIN2Va", getResources().getDrawable(R.drawable.history));
//        search.addSearchable(option1);
//        SearchResult option2 = new SearchResult("EIN2Vb", getResources().getDrawable(R.drawable.history));
//        search.addSearchable(option2);
//        SearchResult option3 = new SearchResult("EIN2Vc", getResources().getDrawable(R.drawable.history));
//        search.addSearchable(option3);

        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(MainActivity.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
                //start searching if there are more then 2 chars.
                if(term.length() > 1) getSearchResults(term);
            }

            @Override
            public void onSearch(String searchTerm) {
                toolbar.setTitle(searchTerm);
                getWeekPager(searchTerm);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
                String title = result.title.substring(0, result.title.indexOf(" ("));
                toolbar.setTitle(title);
//                search.setSearchString(title);

                getWeekPager(title);
            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())toolbar.setTitle("");
    }
}
