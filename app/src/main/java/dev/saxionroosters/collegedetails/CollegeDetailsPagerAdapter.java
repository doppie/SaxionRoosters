package dev.saxionroosters.collegedetails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.saxionroosters.collegedetails.map.MapFragment;
import dev.saxionroosters.general.S;
import dev.saxionroosters.model.College;

/**
 * Created by Jelle on 24-2-2016.
 * Simple FragmentPagerAdapter for the ViewPager
 */
public class CollegeDetailsPagerAdapter extends FragmentStatePagerAdapter {

    private College college;

    public CollegeDetailsPagerAdapter(FragmentManager fm, College college) {
        super(fm);
        this.college = college;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            CollegeDetailsFragment fragment = new CollegeDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable(S.COLLEGE, college);
            fragment.setArguments(args);
            return fragment;
        } else {
            MapFragment fragment = new MapFragment();
            return fragment;
        }
    }


    @Override
    public int getCount() {
        return 1;
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