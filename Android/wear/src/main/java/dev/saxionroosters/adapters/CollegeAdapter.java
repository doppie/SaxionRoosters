package dev.saxionroosters.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.saxionroosters.R;
import dev.saxionroosters.models.College;
import dev.saxionroosters.views.CollegeView;

/**
 * Created by Wessel on 18-11-2016.
 */

public class CollegeAdapter extends WearableListView.Adapter {

    private final Context context;
    private final List<College> items;

    public CollegeAdapter(Context context, List<College> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new WearableListView.ViewHolder(new CollegeView(context));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, final int position) {
        CollegeView collegeView = (CollegeView) viewHolder.itemView;
        final College item = items.get(position);

        TextView textViewTitle = (TextView) collegeView.findViewById(R.id.title);
        textViewTitle.setText(item.title);

        TextView textViewRoom = (TextView) collegeView.findViewById(R.id.room);
        textViewRoom.setText(item.room);

        TextView textViewTime = (TextView) collegeView.findViewById(R.id.time);
        textViewTime.setText(item.time);

        TextView textViewDate = (TextView) collegeView.findViewById(R.id.date);
        textViewDate.setText(item.date);

        View dateColor = (View) collegeView.findViewById(R.id.date_color);

        if (item.color != 0) {
            textViewDate.setVisibility(View.VISIBLE);
            dateColor.setBackgroundColor(item.color);
        } else {
            textViewDate.setVisibility(View.INVISIBLE);
        }

    }

    public void addItem(College college){
        items.add(college);
    }

    public void removeItems(){
        items.clear();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
