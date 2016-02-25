package nl.jelletenbrinke.saxionroosters.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.fragments.WeekFragment;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 * Simple FragmentPagerAdapter for the ViewPager
 */
public class WeekPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Week> weeks;
    private String group;

    public WeekPagerAdapter(FragmentManager fm, ArrayList<Week> weeks, String group) {
        super(fm);
        this.weeks = weeks;
        this.group = group;
    }

    @Override
    public Fragment getItem(int position) {
        Week week = weeks.get(position);

        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putString(S.GROUP, group);
        args.putString(S.WEEK, week.getId());
        fragment.setArguments(args);

        return fragment;
    }

    public void addToFront(Week week) {
        weeks.add(0, week);
        notifyDataSetChanged();
    }

    public void addToBack(Week week) {
        weeks.add(week);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return weeks.size();
    }

    @Override
    public String getPageTitle(int pos) {
        return weeks.get(pos).getName();
    }
}
