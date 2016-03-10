package dev.saxionroosters.activities;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.CollegeDetailsPagerAdapter;
import dev.saxionroosters.adapters.IntroPagerAdapter;

/**
 * Created by Doppie on 10-3-2016.
 */
@EActivity(R.layout.activity_intro)
public class IntroActivity extends BaseActivity {

    @ViewById(R.id.pager)
    protected ViewPager pager;

    @ViewById(R.id.pagerIndicator)
    protected CirclePageIndicator pagerIndicator;

    @ViewById(R.id.skipButton)
    protected Button skipButton;

    //adapters
    private IntroPagerAdapter pagerAdapter;

    @AfterViews
    protected void init() {
        initUI();
    }

    private void initUI() {
        pagerAdapter = new IntroPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
        pagerIndicator.setViewPager(pager);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
