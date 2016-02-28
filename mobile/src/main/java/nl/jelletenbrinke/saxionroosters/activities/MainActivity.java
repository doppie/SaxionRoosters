package nl.jelletenbrinke.saxionroosters.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

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
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Week;


/**
 * The main activity.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    //UI
    @ViewById(R.id.mainLayout)
    protected CoordinatorLayout mainLayout;

    @ViewById(R.id.container)
    protected ViewPager pager;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.searchbox)
    protected SearchBox search;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    private ProgressDialog dialog;

    //adapters
    private WeekPagerAdapter pagerAdapter;

    //data
    @Bean
    protected Storage storage;


    @AfterViews
    protected void init() {
        initUI();
    }

    /* Initializes the UI, called from @onCreate */
    private void initUI() {
        setSupportActionBar(toolbar);

        search.enableVoiceRecognition(this);
//        search.setMenuVisibility(View.GONE);

        //Don't load all pages at once, only load one extra left and right of the current view.
        pager.setOffscreenPageLimit(1);
    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        if(!storage.getCurrentWeeks().isEmpty()) {
            toolbar.setTitle(storage.getCurrentWeeks().get(0).getOwner().getName());
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
        search.clearSearchable();
        //Adding our results
        if(storage.getSearchResults() != null) {
            for(Result result : storage.getSearchResults()) {
                SearchResult option = null;
                if(result.getName() != null && !result.getName().isEmpty()) {
                    option = new SearchResult(result.getAbbrevation() + " (" + result.getName() + ")", getResources().getDrawable(R.drawable.magnify_grey));
                } else if(result.getAbbrevation() != null) {
                    option = new SearchResult(result.getAbbrevation(), getResources().getDrawable(R.drawable.magnify_grey));
                }

               if(option != null) search.addSearchable(option);

            }
        }
        search.updateResults();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == this.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(!matches.isEmpty()) {
                search.populateEditText(matches.get(0));
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openSearch() {
//        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);


        //TODO: First add the search options from history.
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
                Toast.makeText(MainActivity.this, "Menu click", Toast.LENGTH_LONG).show();
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
                if (term.length() > 1) getSearchResults(term);
            }

            @Override
            public void onSearch(String searchTerm) {
//                toolbar.setTitle(searchTerm);
                toolbar.setTitle("");
                getWeekPager(searchTerm);
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
                String title = result.title;
                if (result.title.contains(" (")) {
                    title = result.title.substring(0, result.title.indexOf(" ("));
                }
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
        if(search.getSearchText().isEmpty())toolbar.setTitle(getString(R.string.app_name));
    }

    public ViewPager getPager() {
        return pager;
    }

    public Toolbar getToolbar() {
        return toolbar;
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
