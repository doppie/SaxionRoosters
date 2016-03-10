package dev.saxionroosters.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.CollegeDetailsPagerAdapter;
import dev.saxionroosters.extras.Tools;

/**
 * Created by Doppie on 10-3-2016.
 */

@EActivity(R.layout.activity_college_detail)
public class CollegeDetailActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.tabs)
    protected TabLayout tabLayout;

    @ViewById(R.id.container)
    protected ViewPager pager;

    //adapters
    private CollegeDetailsPagerAdapter pagerAdapter;


    @AfterViews
    protected void init() {
        initUI();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("College details");

        pagerAdapter = new CollegeDetailsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Tools.isLollipop()) supportFinishAfterTransition();
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
