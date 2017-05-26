package dev.saxionroosters.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dev.saxionroosters.Dataset;
import dev.saxionroosters.R;
import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.model.Week;
import dev.saxionroosters.schedulelist.ScheduleListFragment;

/**
 * Created by jelle on 29/11/2016.
 */

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {

    private Dataset dataset;
    private String group;
    private Context context;

    public SchedulePagerAdapter(Context context, FragmentManager fm, String group) {
        super(fm);
        this.context = context;
        this.group = group;
        this.dataset = Dataset.getInstance();
    }

    @Override
    public Fragment getItem(int position) {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putString("group", group);
        args.putInt("offset", Schedule.getOffsetForPos(position));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 26; //26 weeks = half year
    }

    @Override
    public String getPageTitle(int pos) {

        Schedule schedule = dataset.getSchedule(group, Schedule.getOffsetForPos(pos) + "");

        if(pos == 4) {
            return context.getString(R.string.week_current);
        } else if(schedule != null) {
            return schedule.getWeek().getQuartile_week();
        } else {
            return "..";
        }
    }
}
