package dev.saxionroosters;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import java.util.ArrayList;
import java.util.List;

import dev.saxionroosters.adapters.CollegeAdapter;
import dev.saxionroosters.models.College;

public class MainActivity extends Activity implements WearableListView.ClickListener {

    private WearableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);



        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                listView = (WearableListView) stub.findViewById(R.id.list_view);
                loadAdapter();

            }
        });


    }

    private void loadAdapter() {
        List<College> items = new ArrayList<>();
        items.add(new College("1.2: Architectuur & Infrastructuur", "W3.10", "08:30 - 10:00"));
        items.add(new College("1.2: Architectuur & Infrastructuur", "W3.10", "10:15 - 11:45"));
        items.add(new College("1.2: Architectuur & Infrastructuur", "H1.02", "12:30 - 14:45"));

        CollegeAdapter mAdapter = new CollegeAdapter(this, items);

        listView.setAdapter(mAdapter);

        listView.setClickListener(this);
    }


    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        switch (viewHolder.getPosition()) {
            case 0:
                //Do something
                break;
            case 1:
                //Do something else
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {
        //Prevent NullPointerException
    }
}