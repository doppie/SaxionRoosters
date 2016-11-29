package dev.saxionroosters.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.model.Week;
import dev.saxionroosters.schedulelist.ScheduleListFragment;

/**
 * Created by jelle on 29/11/2016.
 */

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private String group;

    public SchedulePagerAdapter(Context context, FragmentManager fm, String group) {
        super(fm);
        this.context = context;
        this.group = group;
    }

    @Override
    public Fragment getItem(int position) {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putString("group", group);
        args.putInt("week", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public String getPageTitle(int pos) {

        //if this is the current week show current week.
        if(pos == 0) {
            return context.getString(R.string.week_current);
        } else {
            return context.getString(R.string.week) + pos;
        }
    }

    @Override
    public int getItemPosition(Object obj) {
        //we dont use this function, as we do not have objects connected to the fragments.
        return -1;
    }
}
