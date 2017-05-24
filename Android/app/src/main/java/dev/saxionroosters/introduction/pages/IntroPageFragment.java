package dev.saxionroosters.introduction.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
import dev.saxionroosters.general.IView;

/**
 * Created by jelle on 30/11/2016.
 */
public class IntroPageFragment extends Fragment implements IView {

    @BindView(R.id.titleText)
    protected TextView titleText;
    @BindView(R.id.subtitleText)
    protected TextView subtitleText;
    @BindView(R.id.introImage)
    protected ImageView introImage;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_intro_page, container, false);
        unbinder = ButterKnife.bind(this, v);

        initUI();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void initUI() {

        Pages page = Pages.valueOf(getArguments().getString("page"));

        switch(page) {
            case HOME:
                titleText.setText(getString(R.string.title_intro_home));
                subtitleText.setText(getString(R.string.subtitle_intro_home));
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_home));
                break;
            case SOCIAL_CODING:
                titleText.setText(getString(R.string.title_intro_social_coding));
                subtitleText.setText(getString(R.string.subtitle_intro_social_coding));
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_github));
                break;
            case MAPS:
                titleText.setText(getString(R.string.title_intro_maps));
                subtitleText.setText(getString(R.string.subtitle_intro_maps));
                introImage.setImageDrawable(getResources().getDrawable(R.drawable.intro_maps));
                break;
        }
    }
}
