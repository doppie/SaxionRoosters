package dev.saxionroosters.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.CollegeDetailsPagerAdapter;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.College;
import dev.saxionroosters.views.JelleTextView;

/**
 * Created by Doppie on 10-3-2016.
 */

@EActivity(R.layout.activity_college_detail)
public class CollegeDetailActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

//    @ViewById(R.id.tabs)
//    protected TabLayout tabLayout;

    @ViewById(R.id.container)
    protected ViewPager pager;

    @ViewById(R.id.nameText)
    protected JelleTextView nameText;

    @ViewById(R.id.timeText)
    protected JelleTextView timeText;

    @ViewById(R.id.locationText)
    protected JelleTextView locationText;

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
        toolbar.setSubtitle(getString(R.string.activity_college_details_subtitle));

        //ensure the backbutton is white on older devices aswell.
        Drawable backButton = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        backButton.setColorFilter(ContextCompat.getColor(this, R.color.cpb_white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backButton);

        Intent i = getIntent();
        College college = null;
        if(i != null) {
            college = (College) i.getSerializableExtra(S.COLLEGE);
            if(college != null) {
                nameText.setText(college.getName());
                timeText.setText(college.getTime());
                locationText.setText(college.getVerticalLocation());
            }
        }

        pagerAdapter = new CollegeDetailsPagerAdapter(getSupportFragmentManager(), college);
        pager.setAdapter(pagerAdapter);
        //commented out for now till we have more tabs to show.
//        tabLayout.setupWithViewPager(pager);
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
