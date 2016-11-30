package dev.saxionroosters.introduction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.saxionroosters.R;
import dev.saxionroosters.introduction.pages.IntroPageFragment;
import dev.saxionroosters.introduction.pages.Pages;
import dev.saxionroosters.searchdialog.SearchDialogFragment;

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

        //the final list of our pager content
        fragments.add(homeFragment);
        fragments.add(gitFragment);
        fragments.add(mapsFragment);
        fragments.add(new SearchDialogFragment());

        pagerAdapter = new IntroductionPagerAdapter(fragments, getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
        //load all screens divide by 2 because 1 limit = left + right screen.
        pager.setOffscreenPageLimit(pagerAdapter.getCount() / 2);
        pagerIndicator.setViewPager(pager);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void moveToFragment(Fragment fragment) {
        int position = pagerAdapter.getPosition(fragment);
        pager.setCurrentItem(position, true); //animations are nice :)
    }

}
