package dev.saxionroosters.schedulelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.saxionroosters.R;
import dev.saxionroosters.collegedetails.CollegeDetailsActivity;
import dev.saxionroosters.general.ClickListener;
import dev.saxionroosters.general.S;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Schedule;
import okhttp3.internal.Util;

/**
 * Created by jelle on 27/11/2016.
 */
public class ScheduleListFragment extends Fragment implements ScheduleListView, ClickListener {

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.loadingLayout)
    RelativeLayout loadingLayout;
    @BindView(R.id.retryLayout)
    RelativeLayout retryLayout;

    private ScheduleListPresenter presenter;
    private ScheduleListAdapter listAdapter;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this class does not need to remember any data, this will all be done in the presenter
        //we receive the extras here, but push them to the presenter.
        int offset = getArguments().getInt("offset", 0); //default = 0
        String group = getArguments().getString("group", ""); //default = ""
        presenter = new ScheduleListPresenter(this, group, offset);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedulelist, container, false);
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
    public void onResume() {
        super.onResume();
        presenter.resume();

        presenter.getSchedule(); //try to load the schedule.
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.pause();
    }

    @Override
    public void initUI() {
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        listAdapter = new ScheduleListAdapter(getContext(), this);
        list.setAdapter(listAdapter);

        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getSchedule();
            }
        });
    }

    @Override
    public void showSchedule(Schedule schedule) {
        listAdapter.updateData(schedule);
        list.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingLayout() {
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRetryLayout() {
        retryLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingLayout() {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void dismissRetryLayout() {
        retryLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        Intent i = new Intent(getContext(), CollegeDetailsActivity.class);
        i.putExtra(S.COLLEGE, (College) listAdapter.getContents().get(position));

        //Add a nice transition animation on newer devices ( > lollipop)
        if(Utils.isLollipop()) {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), v, "collegeCardView");
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

}
