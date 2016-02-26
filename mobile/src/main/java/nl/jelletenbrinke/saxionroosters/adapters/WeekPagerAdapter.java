package nl.jelletenbrinke.saxionroosters.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.fragments.WeekFragment;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 * Simple FragmentPagerAdapter for the ViewPager
 */
public class WeekPagerAdapter extends FragmentStatePagerAdapter {

    private Activity a;
    private ArrayList<Week> weeks;

    public WeekPagerAdapter(Activity a, FragmentManager fm, ArrayList<Week> weeks) {
        super(fm);
        this.a = a;
        this.weeks = weeks;
    }

    @Override
    public Fragment getItem(int position) {
        Week week = weeks.get(position);

        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putString(week.getOwner().getTypeName(), week.getOwner().getName());
        args.putString(S.WEEK_ID, week.getId());
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
        Week week = weeks.get(pos);

        //If current week add this to the page title.
        if(week.getId().equals("0")) {
            return a.getString(R.string.this_week) + "(" + week.getName() + ")";
        }
        return weeks.get(pos).getName();
    }

    @Override
    public int getItemPosition(Object week) {
        return weeks.indexOf(week);
    }
}
