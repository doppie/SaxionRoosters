package dev.saxionroosters.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.fragments.IntroFragment;
import dev.saxionroosters.fragments.IntroFragment_;
import dev.saxionroosters.fragments.SetupFragment;
import dev.saxionroosters.fragments.SetupFragment_;

/**
 * Created by Doppie on 10-3-2016.
 */
public class IntroPagerAdapter extends FragmentPagerAdapter {

    private Activity a;

    public IntroPagerAdapter(FragmentManager fm, Activity a) {
        super(fm);
        this.a = a;

    }
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = new IntroFragment_();
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                args.putString(S.TITLE, a.getString(R.string.intro_title_1));
                args.putString(S.SUBTITLE, a.getString(R.string.intro_subtitle_1));
                args.putString(S.IMAGE_NAME, "intro_home");
                break;
            case 1:
                args.putString(S.TITLE, a.getString(R.string.intro_title_5));
                args.putString(S.SUBTITLE, a.getString(R.string.intro_subtitle_5));
                args.putString(S.IMAGE_NAME, "intro_maps");
                break;
            case 2:
                args.putString(S.TITLE, a.getString(R.string.intro_title_2));
                args.putString(S.SUBTITLE, a.getString(R.string.intro_subtitle_2));
                args.putString(S.IMAGE_NAME, "intro_github");
                break;
            case 3:
                fragment = new SetupFragment_();
                args.putString(S.VIEW_TYPE, S.FRAGMENT);
                break;
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
