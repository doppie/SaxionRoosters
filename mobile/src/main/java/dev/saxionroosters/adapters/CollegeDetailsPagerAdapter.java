package dev.saxionroosters.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.fragments.CollegeDetailsFragment;
import dev.saxionroosters.fragments.CollegeDetailsFragment_;
import dev.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 * Simple FragmentPagerAdapter for the ViewPager
 */
public class CollegeDetailsPagerAdapter extends FragmentStatePagerAdapter {



    public CollegeDetailsPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        CollegeDetailsFragment fragment = new CollegeDetailsFragment_();
//        WeekFragment fragment = new WeekFragment_();
//        Bundle args = new Bundle();
//        args.putString(week.getOwner().getTypeName(), week.getOwner().getName());
//        args.putString(S.WEEK_ID, week.getId());
//        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public String getPageTitle(int pos) {
        if(pos == 0) {
            return "Details";
        } else if(pos == 1) {
            return "Locatie";
        } else return "Unknown";
    }
}
