package dev.saxionroosters.schedulelist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.saxionroosters.R;
import dev.saxionroosters.general.ClickListener;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.model.College;
import dev.saxionroosters.model.Day;
import dev.saxionroosters.model.Schedule;

/**
 * Created by jelle on 28/11/2016.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Object> contents;
    private ClickListener clickListener;

    public ScheduleListAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.contents = new ArrayList<>();
        this.clickListener = clickListener;
    }

    /**
     * Replaces the list contents with the schedule parameter
     * And notifies the adapter that its contents has changed.
     *
     * @param schedule
     */
    public void updateData(Schedule schedule) {
        contents.clear();

        //we want the Day for the header and its children objects Colleges.
        for(Day d : schedule.getDays()) {
            contents.add(d);
            contents.addAll(d.getColleges());
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_college, parent, false);

        return new ViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object object = contents.get(position);

        if (object instanceof Day) {
            Day day = (Day) object;

            //check if today is a holiday.
            if (day.getDate().getHoliday() != null && !day.getDate().getHoliday().isEmpty()) {
                holder.freeDayView.setVisibility(View.VISIBLE);
                holder.freeDayView.setText(context.getString(R.string.holiday));
            } else if (day.getDate().getVacation() != null && !day.getDate().getVacation().isEmpty()) {
                holder.freeDayView.setVisibility(View.VISIBLE);
                holder.freeDayView.setText(context.getString(R.string.vacation));
            } else if (day.getColleges().isEmpty()) {
                holder.freeDayView.setVisibility(View.VISIBLE);
                holder.freeDayView.setText(context.getString(R.string.free_day));
            } else {
                holder.freeDayView.setVisibility(View.GONE);
            }

            //Disable the college item view
            holder.cardView.setVisibility(View.GONE);

            //Jup this is a date divider
            holder.line.setVisibility(View.VISIBLE);
            holder.dateText.setVisibility(View.VISIBLE);


            try {
                Date date = Utils.getDateFormatter().parse(day.getDate().getDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                holder.dateText.setText(Utils.getFullDateFormatter().format(cal.getTime()));
            } catch (ParseException e) {
                holder.dateText.setText("Error - unparseable date");
            }

        } else if (object instanceof College) {
            College college = (College) object;

            //Disable unnecesary view(date divider)
            holder.line.setVisibility(View.GONE);
            holder.dateText.setVisibility(View.GONE);
            holder.freeDayView.setVisibility(View.GONE);

            //Enable the cardView that displays a college item.
            holder.cardView.setVisibility(View.VISIBLE);

            holder.nameText.setText(college.getName());
            holder.timeText.setText(college.getStart() + " - " + college.getEnd() );
            holder.locationText.setText(college.getRoom());
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public ArrayList<Object> getContents() {
        return contents;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.categoryColor)
        View categoryColor;
        @BindView(R.id.line)
        View line;
        @BindView(R.id.nameText)
        TextView nameText;
        @BindView(R.id.timeText)
        TextView timeText;
        @BindView(R.id.dateText)
        TextView dateText;
        @BindView(R.id.locationText)
        TextView locationText;
        @BindView(R.id.freeDayView)
        TextView freeDayView;

        private ClickListener clickListener;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);
            ButterKnife.bind(this, v);
            this.clickListener = clickListener;
            this.cardView.setOnClickListener(this);
            this.cardView.setOnLongClickListener(this);
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