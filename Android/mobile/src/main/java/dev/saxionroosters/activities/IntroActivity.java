package dev.saxionroosters.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.adapters.CollegeDetailsPagerAdapter;
import dev.saxionroosters.adapters.IntroPagerAdapter;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.ThemeTools;

/**
 * Created by Doppie on 10-3-2016.
 */
@EActivity(R.layout.activity_intro)
public class IntroActivity extends BaseActivity {

    @ViewById(R.id.mainLayout)
    protected CoordinatorLayout mainLayout;

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
        //always set the basic theme to Green from now on, much better.
        if(storage.getObject(S.SETTING_THEME_COLOR) == null || storage.getObject(S.SETTING_THEME_COLOR).isEmpty()) {
            storage.saveObject(S.SETTING_THEME_COLOR, "Green");
            ThemeTools.activateTheme(this);
        }

        initUI();
    }

    private void initUI() {
        pagerAdapter = new IntroPagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
        //load all screens divide by 2 because 1 limit = left + right screen.
        pager.setOffscreenPageLimit(pagerAdapter.getCount() / 2);
        pagerIndicator.setViewPager(pager);
    }

    @Click(R.id.skipButton)
    void skipButtonClicked() {
        if(storage.getObject(S.SETTING_STARTUP_OWNER) == null
                || ((String) storage.getObject(S.SETTING_STARTUP_OWNER)).isEmpty()) {

            //smooth scroll to the last item.
            pager.setCurrentItem(pagerAdapter.getCount() - 1, true);
            Snackbar.make(mainLayout, getString(R.string.error_intro_select_startup_owner), Snackbar.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(IntroActivity.this, MainActivity_.class);
            startActivity(i);
            finish();
        }
    }

}
