package nl.jelletenbrinke.saxionroosters.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.adapters.WeekPagerAdapter;
import nl.jelletenbrinke.saxionroosters.extras.NetworkAsyncTask;
import nl.jelletenbrinke.saxionroosters.extras.S;
import nl.jelletenbrinke.saxionroosters.interfaces.OnAsyncTaskCompleted;
import nl.jelletenbrinke.saxionroosters.model.Week;


/**
 * The main activity.
 */
public class MainActivity extends AppCompatActivity implements OnAsyncTaskCompleted {

    private WeekPagerAdapter pagerAdapter;
    private ViewPager pager;
    private TabLayout tabLayout;

    private String group = "EIN2Va";
    private ArrayList<Week> weeks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NetworkAsyncTask getWeekPager = new NetworkAsyncTask(this);
        String url = S.URL + S.SCHEDULE + "/" + S.GROUP + ":" + group + "/" + S.WEEK + ":" + "0";
        getWeekPager.execute(url, S.PARSE_WEEK_PAGER);


        initUI();
    }

    /* Initializes the UI, called from @onCreate */
    private void initUI() {
        pager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    /* When called this updates all UI items that contain data.  */
    private void updateUI() {
        pagerAdapter = new WeekPagerAdapter(getSupportFragmentManager(), weeks, "EIN2Va");
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAsyncTaskCompleted(Object obj) {

        //We received the empty weeks for the pager!
        ArrayList<Week> newWeeks = (ArrayList<Week>) obj;
        this.weeks = newWeeks;

        updateUI();
    }
}
