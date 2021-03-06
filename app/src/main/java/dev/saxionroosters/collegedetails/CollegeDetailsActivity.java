package dev.saxionroosters.collegedetails;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.general.S;
import dev.saxionroosters.general.ThemeUtils;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.model.College;
import dev.saxionroosters.settings.Settings;

/**
 * Created by Jelle on 22/05/2017.
 */

public class CollegeDetailsActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager pager;
    @BindView(R.id.nameText)
    TextView nameText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.locationText)
    TextView locationText;
    private Unbinder unbinder;

    private CollegeDetailsPagerAdapter pagerAdapter;

    private College college;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onCreateSetTheme(this, PreferenceManager.getInstance(this).read(Settings.THEME_COLOR));
        setContentView(R.layout.activity_details_college);
        unbinder = ButterKnife.bind(this);

        loadCollege();

        setToolbar();
        setPager();
        showCollege();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setSubtitle(getString(R.string.title_college_details));

        Drawable backButton = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_white_24dp, null);
        getSupportActionBar().setHomeAsUpIndicator(backButton);
    }

    private void setPager() {
        pagerAdapter = new CollegeDetailsPagerAdapter(getSupportFragmentManager(), college);
        pager.setAdapter(pagerAdapter);

        //To enable the tabs
        //tabLayout.setupWithViewPager(pager);
    }

    /**
     * Reads the College object from the Intent.
     */
    private void loadCollege() {
        Intent i = getIntent();
        if (i != null) {
            college = (College) i.getSerializableExtra(S.COLLEGE);
        }
    }

    /**
     * Shows the college data (if one is loaded).
     */
    private void showCollege() {
        if (college != null) {
            nameText.setText(college.getName());
            timeText.setText(college.getStart() + " - " + college.getEnd());
            locationText.setText(college.getRoom());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Nice transition animation on newer devices ( > lollipop)
        if(Utils.isLollipop()) supportFinishAfterTransition();
        else finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
