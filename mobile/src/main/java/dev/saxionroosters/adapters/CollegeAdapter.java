package dev.saxionroosters.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dev.saxionroosters.interfaces.ClickListener;
import dev.saxionroosters.R;
import dev.saxionroosters.model.College;

/**
 * Created by Doppie on 25-2-2015.
 * Custom adapter for College items
 * Used on a recyclerview. Nope we will no longer use ListView.
 */
public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ViewHolder> {

    private ArrayList<College> data;
    private ClickListener clickListener;

    public CollegeAdapter(ArrayList<College> data, ClickListener clickListener) {
        this.data = data;
        this.clickListener = clickListener;
    }

    public void setData(ArrayList<College> newData) {
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    public ArrayList<College> getData() {
        return data;
    }

    public void remove(int position) {
        if(position >= data.size()) return;
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void removeAll() {
        for(int i = 0; i < data.size(); i++) {
            data.remove(i);
            notifyItemRemoved(i);
        }
    }

    public void add(int position, College item) {
        data.add(position, item);
        notifyItemInserted(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //initialize the view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_college, parent, false);
        ViewHolder h = new ViewHolder(v, clickListener);
        h.cardView = (CardView) v.findViewById(R.id.card_view);
        h.nameText = (TextView) v.findViewById(R.id.nameText);
        h.timeText = (TextView) v.findViewById(R.id.timeText);
        h.dateText = (TextView) v.findViewById(R.id.dateText);
        h.freeDayView = (TextView) v.findViewById(R.id.freeDayView);
        h.categoryColor = (View) v.findViewById(R.id.categoryColor);
        h.locationText = (TextView) v.findViewById(R.id.locationText);
        h.line = (View) v.findViewById(R.id.line);

        h.cardView.setClickable(true);
        h.cardView.setOnClickListener(h);
        h.cardView.setOnLongClickListener(h);
        
        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        College college = data.get(position);

        if(college.getName() != null) {

            //Disable unnecesary view(date divider)
            holder.line.setVisibility(View.GONE);
            holder.dateText.setVisibility(View.GONE);
            holder.freeDayView.setVisibility(View.GONE);

            //Enable the cardView that displays a college item.
            holder.cardView.setVisibility(View.VISIBLE);

            holder.nameText.setText(college.getName());
            holder.timeText.setText(college.getTime());
            holder.locationText.setText(college.getVerticalLocation());


        } else {

            if(college.isFreeDay()) {
                holder.freeDayView.setVisibility(View.VISIBLE);
            } else {
                holder.freeDayView.setVisibility(View.GONE);
            }
            //Disable the college item view
            holder.cardView.setVisibility(View.GONE);

            //Jup this is a date divider
            holder.line.setVisibility(View.VISIBLE);
            holder.dateText.setVisibility(View.VISIBLE);

            holder.dateText.setText(college.getDate());
        }
//        holder.categoryColor.setBackgroundResource(item.getCategory().getColor());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View v, categoryColor, line;
        public CardView cardView;
        public TextView nameText, timeText, dateText, locationText, freeDayView;
        public ClickListener clickListener;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);
            this.v = v;
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

}
