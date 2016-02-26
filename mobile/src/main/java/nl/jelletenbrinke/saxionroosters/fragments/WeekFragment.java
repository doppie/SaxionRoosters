package nl.jelletenbrinke.saxionroosters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.adapters.CollegeAdapter;
import nl.jelletenbrinke.saxionroosters.extras.NetworkAsyncTask;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.interfaces.ClickListener;
import nl.jelletenbrinke.saxionroosters.interfaces.OnAsyncTaskCompleted;
import nl.jelletenbrinke.saxionroosters.model.College;
import nl.jelletenbrinke.saxionroosters.model.Day;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 */
public class WeekFragment extends Fragment implements ClickListener, OnAsyncTaskCompleted {


    //UI
    private RelativeLayout mainLayout;
    private RecyclerView list;
    private CollegeAdapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;
    private RelativeLayout loadingLayout, retryLayout;

    //data
    private String group, weekId;
    private Week week;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_week, container, false);

        initUI(v);

        //Reads the arguments to know which week should be loaded :)
        Bundle args = getArguments();
        group = args.getString(S.GROUP);
        weekId = args.getString(S.WEEK);

        if (group == null || weekId == null) Log.e("debug", "oh noes, this can never be null :O");


        //Run the task :)
        String url = S.URL + S.SCHEDULE + "/" + S.GROUP + ":" + group + "/" + S.WEEK + ":" + weekId;
        NetworkAsyncTask task = new NetworkAsyncTask(this, getActivity(), false);
        task.execute(url, S.PARSE_WEEK, group, weekId);
        //also show loading dialog :)
        showLoading();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
    }

    /* Initializes the UI, called from @onCreateView */
    private void initUI(View v) {
        mainLayout = (RelativeLayout) v.findViewById(R.id.mainLayout);
        list = (RecyclerView) v.findViewById(R.id.list);
        //fixed size always true: important for performance!
        list.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(listLayoutManager);

        //This one is used whenever an item is removed or added to the adapter
        //It animates the item nicely :)
        list.setItemAnimator(new DefaultItemAnimator());

        listAdapter = new CollegeAdapter(new ArrayList<College>(), this);
        //Sets an empty adapter.
        list.setAdapter(listAdapter);

        loadingLayout = (RelativeLayout) v.findViewById(R.id.loadingLayout);
        retryLayout = (RelativeLayout) v.findViewById(R.id.retryLayout);
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Run the task :)
                String url = S.URL + S.SCHEDULE + "/" + S.GROUP + ":" + group + "/" + S.WEEK + ":" + weekId;
                NetworkAsyncTask task = new NetworkAsyncTask(WeekFragment.this, getActivity(), false);
                task.execute(url, S.PARSE_WEEK, group, weekId);
                //also show loading dialog :)
                showLoading();
            }
        });

    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        //TODO: error handling plsss.
        if (week == null) {
            showRetry();
            return;
        }
        listAdapter.removeAll();

        //Add all colleges to the adapter
        final ArrayList<College> colleges = new ArrayList<>();
        for (Day day : week.getDays()) {
            //For every day add a "divider date" college object
            College dividerDate = new College(day.getName());
            colleges.add(dividerDate);
            colleges.addAll(day.getColleges());
        }
        listAdapter.setData(colleges);
        showList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(int position, boolean isLongClick) {
        College college = listAdapter.getData().get(position);

        //Do something here!!
        if (isLongClick) {

        }
    }

    @Override
    public void onAsyncTaskCompleted(Object object) {

        //We received a (full) week object show the schedule to the user :D
        if (object instanceof Week) {
            this.week = (Week) object;
            updateUI();
        } else if(object instanceof Exception) {
            //ok this is not the right way, but for now its ok.
            //for all errors show the retry button :)
            Snackbar.make(mainLayout, "No internet connection.", Snackbar.LENGTH_SHORT).show();

            showRetry();
        }
    }

    //TODO: We can add nice fade-in/out animations here.
    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
    }

    //TODO: We can add nice fade-in/out animations here.
    private void showList() {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);
    }

    //TODO: We can add nice fade-in/out animations here.
    private void showRetry() {
        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }
}