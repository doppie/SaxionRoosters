package dev.saxionroosters.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import dev.saxionroosters.R;

/**
 * Created by Wessel on 18-11-2016.
 */

public class CollegeView extends FrameLayout implements WearableListView.OnCenterProximityListener {

    final TextView title;
    final TextView room;
    final TextView time;
    final TextView date;
    final CardView card;

    public CollegeView(Context context) {
        super(context);
        View.inflate(context, R.layout.college_item, this);
        title = (TextView) findViewById(R.id.title);
        room = (TextView) findViewById(R.id.room);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        card = (CardView) findViewById(R.id.card_view);
    }


    @Override
    public void onCenterPosition(boolean b) {
        // Animation when the view becomes centered
        title.animate().scaleX(1f).scaleY(1f).alpha(1);
        room.animate().scaleX(1f).scaleY(1f).alpha(1);
        time.animate().scaleX(1f).scaleY(1f).alpha(1);
        card.animate().scaleX(1f).scaleY(1f).alpha(1);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        // Animation when the view is not centered anymore
        title.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.8f);
        room.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.8f);
        time.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.8f);
        card.animate().scaleX(0.95f).scaleY(0.95f).alpha(0.8f);
    }
}