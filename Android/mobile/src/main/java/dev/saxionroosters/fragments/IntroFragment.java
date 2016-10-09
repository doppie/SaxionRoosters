package dev.saxionroosters.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import dev.saxionroosters.R;
import dev.saxionroosters.extras.S;

/**
 * Created by Doppie on 10-3-2016.
 */
@EFragment(R.layout.fragment_intro)
public class IntroFragment extends Fragment {

    @ViewById(R.id.titleText)
    protected TextView titleText;

    @ViewById(R.id.subtitleText)
    protected TextView subtitleText;

    @ViewById(R.id.introImage)
    protected ImageView introImage;

    @AfterViews
    protected void init() {

        Bundle args = getArguments();
        String title = args.getString(S.TITLE);
        String subtitle = args.getString(S.SUBTITLE);
        String imageName = args.getString(S.IMAGE_NAME);

        if(title != null) titleText.setText(title);
        if(subtitle != null) subtitleText.setText(subtitle);
        if(imageName != null) {
            if(imageName.equals("intro_home")) {
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_home));
            } else if(imageName.equals("intro_github")) {
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_github));
            } else if(imageName.equals("intro_maps")) {
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_maps));
            }
        }

    }
}
