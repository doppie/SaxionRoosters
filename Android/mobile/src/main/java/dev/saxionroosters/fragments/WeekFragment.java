package dev.saxionroosters.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dev.saxionroosters.R;
import dev.saxionroosters.activities.BaseActivity;
import dev.saxionroosters.activities.CollegeDetailActivity;
import dev.saxionroosters.activities.CollegeDetailActivity_;
import dev.saxionroosters.adapters.CollegeAdapter;
import dev.saxionroosters.adapters.DatabaseAdapter;
import dev.saxionroosters.extras.HtmlRetriever;
import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Storage;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.interfaces.ClickListener;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Day;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Week;

/**
 * Created by Doppie on 24-2-2016.
 */
@EFragment(R.layout.fragment_week)
public class WeekFragment extends Fragment implements ClickListener {


    //UI
    @ViewById(R.id.mainLayout)
    protected RelativeLayout mainLayout;

    @ViewById(R.id.list)
    protected RecyclerView list;

    @ViewById(R.id.loadingLayout)
    protected RelativeLayout loadingLayout;

    @ViewById(R.id.retryLayout)
    protected RelativeLayout retryLayout;

    private CollegeAdapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;

    //data
    private Week week;

    @Bean
    protected Storage storage;

    /* Initializes the UI after views are injected */
    @AfterViews
    protected void initUI() {
        //Reads the arguments to know which week should be loaded :)
        Bundle args = getArguments();
        week = storage.getWeekById(args.getString(S.WEEK_ID));

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

        //Run the task if this is an empty week object, else just load the existing week.
        if(week == null || week.getDays() == null || week.getDays().isEmpty() || week.getOwner() == null) {
            getWeekTask();
        } else {
            updateUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.log("onResume");
        updateUI();
    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        //TODO: error handling plsss.
        if (week == null || week.getDays() == null || week.getDays().isEmpty()) {
            showRetry();
            return;
        }
        listAdapter.removeAll();



        //get the current day
        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_WEEK)-1; //-1 because we compare to a list which starts at 0
        int currentDayPosition = 0;

        //Add all colleges to the adapter
        final ArrayList<College> colleges = new ArrayList<>();
        for (int i = 0; i < week.getDays().size(); i++) {
            Day day = week.getDays().get(i);

            //if the currentDay is found we want to know the position
            //of the date divider in the college list.
            //we use this to scroll to the current day.
            if(i == currentDay) currentDayPosition = colleges.size();

            //For every day add a "divider date" college object
            boolean showFreeDay = false;
            if(day.getColleges().isEmpty()) {
                showFreeDay = true;
            }

            College dividerDate = new College(day.getDate(), showFreeDay);
            colleges.add(dividerDate);

            colleges.addAll(day.getColleges());
        }
        listAdapter.setData(colleges);


        //make the list visible
        showList();

        //after the list is shown we can scroll to the current day.
        //subtract one item extra so we are sure the correct day is on the screen.
        //fixes issue where the date divider is hidden under the toolbar.
        list.getLayoutManager().scrollToPosition(currentDayPosition-1);
    }

    @Background
    protected void getWeekTask() {
        preExecute();

        HtmlRetriever retriever = new HtmlRetriever(getActivity(), week);
        String url = S.URL + S.SCHEDULE + "/" + week.getOwner().getTypeName() + ":" + week.getOwner().getName() + "/" + S.WEEK_ID + ":" + week.getId();
        Object object = retriever.retrieveHtml(url, S.PARSE_WEEK);

        postExecute(retriever, object);
    }

    @UiThread
    protected void postExecute(HtmlRetriever retriever, Object object) {
        Week week = retriever.onWeekScheduleRetrieveCompleted(object);
        if(week != null) {
            this.week = week;
            updateUI();
        }
        else {
            Snackbar.make(mainLayout, getString(R.string.error_title_no_internet), Snackbar.LENGTH_SHORT).show();
            showRetry();
        }
    }

    @UiThread
    protected void preExecute() {
        loadingLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Click(R.id.retryLayout)
    protected void onRetryClick() {
        getWeekTask();
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        College college = listAdapter.getData().get(position);

        //Do something here!!
        if (isLongClick) {
//            boolean success = storage.addNotification(college.getName(), college.getDate(), new Date(System.currentTimeMillis() + 5000));
//            if(success) ((BaseActivity) getActivity()).scheduleNotification(((BaseActivity) getActivity()).getNotification(""), 5000);
//            else Tools.log("NOOO! Couldnt save notification in DB");
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), v, "collegeCardView");
            Intent i = new Intent(getActivity(), CollegeDetailActivity_.class);
            i.putExtra(S.COLLEGE, college);
            if(Tools.isLollipop()) startActivity(i, options.toBundle());
            else startActivity(i);
        }
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