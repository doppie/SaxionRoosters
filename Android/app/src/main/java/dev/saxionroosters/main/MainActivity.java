package dev.saxionroosters.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.saxionroosters.R;
import dev.saxionroosters.introduction.IntroductionActivity;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";

    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.pager)
    ViewPager pager;

    private MainPresenter presenter;
    private SchedulePagerAdapter pagerAdapter;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUI();

        presenter = new MainPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void initUI() {
        setSupportActionBar(toolbar);

        pager.setOffscreenPageLimit(1);
        tabs.setupWithViewPager(pager);

        searchView.setVersion(SearchView.VERSION_MENU_ITEM);
        searchView.setTheme(SearchView.THEME_LIGHT, true);
        searchView.setAnimationDuration(300);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.search(query);
                searchView.close(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) presenter.search(newText);
                return false;
            }
        });
        searchAdapter = new SearchAdapter(this, new ArrayList<SearchItem>());
        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchItem item = searchAdapter.getSuggestionsList().get(position);
                showSchedulePager(item.get_text().toString());
                searchView.close(true);
            }
        });
        searchView.setAdapter(searchAdapter);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showSchedulePager(String group) {

        //reset the adapter with the new group.
        pager.setAdapter(null);
        pagerAdapter = null;
        pagerAdapter = new SchedulePagerAdapter(this, getSupportFragmentManager(), group);
        pager.setAdapter(pagerAdapter);

        //set our header subtitle
        getSupportActionBar().setSubtitle(group);

        pager.setCurrentItem(4); //current week, as we want to be able to look back 5 weeks.
    }

    @Override
    public void updateTabTitle(int pos, String title) {
        TabLayout.Tab tab = tabs.getTabAt(pos);
        tab.setText(title);
    }

    @Override
    public void showSearchResults(ArrayList<SearchItem> results) {
        searchAdapter.setSuggestionsList(results);
    }

    @Override
    public void showInterstitialAd() {

    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                searchView.open(true);
                return true;
        }

        return false;
    }
}
