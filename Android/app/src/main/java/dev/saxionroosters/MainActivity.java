package dev.saxionroosters;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import dev.saxionroosters.schedulelist.ScheduleListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is only a test implementation
        Fragment testFragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putString("group", "EIB2a");
        args.putInt("week", 0);
        testFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.content, testFragment).commit();
    }
}
