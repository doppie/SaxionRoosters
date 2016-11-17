package dev.saxionroosters.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dev.saxionroosters.R;
import dev.saxionroosters.models.College;
import dev.saxionroosters.views.CollegeView;

/**
 * Created by Wessel on 17-11-2016.
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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
