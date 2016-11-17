package dev.saxionroosters.views;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import dev.saxionroosters.R;

/**
 * Created by Wessel on 17-11-2016.
 */

public class CollegeView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final TextView title;
    final TextView room;
    final TextView time;

    public CollegeView(Context context) {
        super(context);
        View.inflate(context, R.layout.college_item, this);
        title = (TextView) findViewById(R.id.title);
        room = (TextView) findViewById(R.id.room);
        time = (TextView) findViewById(R.id.time);
    }


    @Override
    public void onCenterPosition(boolean b) {

        //Animation example to be ran when the view becomes the centered one
        //title.animate().scaleX(1f).scaleY(1f).alpha(1);
        //room.animate().scaleX(1f).scaleY(1f).alpha(1);
        //time.animate().scaleX(1f).scaleY(1f).alpha(1);

    }

    @Override
    public void onNonCenterPosition(boolean b) {

        //Animation example to be ran when the view is not the centered one anymore
        //title.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
        //room.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
        //time.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);

    }
}