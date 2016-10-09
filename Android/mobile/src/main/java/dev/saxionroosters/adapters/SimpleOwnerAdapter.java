package dev.saxionroosters.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dev.saxionroosters.R;
import dev.saxionroosters.interfaces.ClickListener;
import dev.saxionroosters.model.Group;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Teacher;

/**
 * Created by Doppie on 1-3-2016.
 */
public class SimpleOwnerAdapter extends RecyclerView.Adapter<SimpleOwnerAdapter.ViewHolder> {

    private ArrayList<Owner> data;
    private ClickListener clickListener;

    public SimpleOwnerAdapter(ArrayList<Owner> data, ClickListener clickListener) {
        this.data = data;
        this.clickListener = clickListener;
    }

    public void setData(ArrayList<Owner> newData) {
        data.clear();
        data.addAll(newData);
        this.notifyDataSetChanged();
    }

    public ArrayList<Owner> getData() {
        return data;
    }

    public void add(int position, Owner item) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //initialize the view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_simple_item_layout, parent, false);
        ViewHolder h = new ViewHolder(v, clickListener);

        h.nameText = (TextView) v.findViewById(R.id.nameText);
        h.courseText = (TextView) v.findViewById(R.id.courseText);

        v.setClickable(true);
        v.setOnClickListener(h);
        v.setOnLongClickListener(h);

        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Owner owner = data.get(position);



        if(owner.getName() != null) {
            //this is a normal owner item.

            if(owner instanceof Group) {
                Group group = (Group) owner;
                holder.nameText.setText(group.getName());
                holder.courseText.setText(group.getCourseName());
            } else if(owner instanceof Teacher) {
                Teacher teacher = (Teacher) owner;
                holder.nameText.setText(teacher.getName());
                holder.courseText.setText(teacher.getIdName());
            }
        } else {
            holder.nameText.setText("Unknown");
            holder.courseText.setText("Please contact the developers.");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public View v, parentView;
        public TextView nameText, courseText;
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
