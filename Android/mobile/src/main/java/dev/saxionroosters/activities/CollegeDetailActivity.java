package dev.saxionroosters.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.CollegeDetailsPagerAdapter;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.College;


/**
 * Created by Doppie on 10-3-2016.
 */

@EActivity(R.layout.activity_college_detail)
public class CollegeDetailActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.adView)
    protected AdView adView;

//    @ViewById(R.id.tabs)
//    protected TabLayout tabLayout;

    @ViewById(R.id.container)
    protected ViewPager pager;

    @ViewById(R.id.nameText)
    protected TextView nameText;

    @ViewById(R.id.timeText)
    protected TextView timeText;

    @ViewById(R.id.locationText)
    protected TextView locationText;

    //adapters
    private CollegeDetailsPagerAdapter pagerAdapter;


    @AfterViews
    protected void init() {
        initUI();
        initAds();
    }

    private void initUI() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setSubtitle(getString(R.string.activity_college_details_subtitle));

        Drawable backButton = VectorDrawableCompat.create(getResources(), R.drawable.search_ic_arrow_back_black_24dp, null);
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

    private void initAds() {
        if(Boolean.valueOf(storage.getObject("premium"))) return;
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("96CC90CB12D776D19FA21597DD1A2202")
                .build();
        adView.loadAd(adRequest);

        initInterstitialAd();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitialAd();
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
