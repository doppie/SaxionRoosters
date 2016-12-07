package dev.saxionroosters.introduction;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.saxionroosters.R;
import dev.saxionroosters.general.PreferenceManager;
import dev.saxionroosters.general.ThemeUtils;
import dev.saxionroosters.introduction.pages.IntroPageFragment;
import dev.saxionroosters.introduction.pages.Pages;
import dev.saxionroosters.searchdialog.SearchDialogFragment;
import dev.saxionroosters.settings.Settings;

/**
 * Created by jelle on 30/11/2016.
 */

public class IntroductionActivity extends AppCompatActivity implements IntroductionView {

    @BindView(R.id.mainLayout)
    CoordinatorLayout mainLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.pagerIndicator)
    CirclePageIndicator pagerIndicator;
    @BindView(R.id.skipButton)
    Button skipButton;

    private IntroductionPresenter presenter;
    private IntroductionPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onCreateSetTheme(this, PreferenceManager.getInstance(this).read(Settings.THEME_COLOR));
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);

        presenter = new IntroductionPresenter(this);

        initUI();

    }

    @Override
    public void initUI() {

        ArrayList<Fragment> fragments = new ArrayList<>();

        IntroPageFragment homeFragment = new IntroPageFragment();
        Bundle homeArgs = new Bundle();
        homeArgs.putString("page", Pages.HOME.toString());
        homeFragment.setArguments(homeArgs);

        IntroPageFragment mapsFragment = new IntroPageFragment();
        Bundle mapsArgs = new Bundle();
        mapsArgs.putString("page", Pages.MAPS.toString());
        mapsFragment.setArguments(mapsArgs);

        IntroPageFragment gitFragment = new IntroPageFragment();
        Bundle gitArgs = new Bundle();
        gitArgs.putString("page", Pages.SOCIAL_CODING.toString());
        gitFragment.setArguments(gitArgs);

        SearchDialogFragment searchFragment = new SearchDialogFragment();
        Bundle searchArgs = new Bundle();
        searchArgs.putBoolean("dialog", false);
        searchFragment.setArguments(searchArgs);

        //the final list of our pager content
        fragments.add(homeFragment);
        fragments.add(gitFragment);
        fragments.add(mapsFragment);
        fragments.add(searchFragment);

        pagerAdapter = new IntroductionPagerAdapter(fragments, getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
        //load all screens divide by 2 because 1 limit = left + right screen.
        pager.setOffscreenPageLimit(pagerAdapter.getCount() / 2);
        pagerIndicator.setViewPager(pager);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.skipIntroduction();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void moveToLastFragment() {
        pager.setCurrentItem(pagerAdapter.getCount(), true);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

}
