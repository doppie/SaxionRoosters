package dev.saxionroosters.introduction;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import dev.saxionroosters.R;

/**
 * Created by jelle on 30/11/2016.
 */

public class IntroductionPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> contents;
    private Activity a;

    public IntroductionPagerAdapter(ArrayList<Fragment> contents, FragmentManager fm, Activity a) {
        super(fm);
        this.contents = contents;
        this.a = a;

    }
    @Override
    public Fragment getItem(int position) {
        return contents.get(position);
    }

    /**
     * Returns the position in the adapter for this fragment.
     * @param fragment
     * @return
     */
    public int getPosition(Fragment fragment) {
        return contents.indexOf(fragment);
    }

    @Override
    public int getCount() {
        return contents.size();
    }
}
