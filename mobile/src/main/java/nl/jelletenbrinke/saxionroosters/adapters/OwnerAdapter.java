package nl.jelletenbrinke.saxionroosters.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.R;
import nl.jelletenbrinke.saxionroosters.interfaces.ClickListener;
import nl.jelletenbrinke.saxionroosters.model.Owner;

/**
 * Created by Doppie on 1-3-2016.
 */
public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.ViewHolder> {

    private ArrayList<Owner> data;
    private ClickListener clickListener;

    public OwnerAdapter(ArrayList<Owner> data, ClickListener clickListener) {
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

    public void add(int position, Owner item) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //initialize the view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_item_layout, parent, false);
        ViewHolder h = new ViewHolder(v, clickListener);
        h.cardView = (CardView) v.findViewById(R.id.card_view);
        h.nameText = (TextView) v.findViewById(R.id.nameText);
        h.courseText = (TextView) v.findViewById(R.id.courseText);
        h.ownerTypeText = (TextView) v.findViewById(R.id.ownerTypeText);
        h.categoryColor = (View) v.findViewById(R.id.categoryColor);
        h.line = (View) v.findViewById(R.id.line);

        h.cardView.setClickable(true);
        h.cardView.setOnClickListener(h);
        h.cardView.setOnLongClickListener(h);

        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Owner owner = data.get(position);

        if(owner.getName() != null) {
            holder.line.setVisibility(View.GONE);
            holder.ownerTypeText.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);

            holder.nameText.setText(owner.getName());
            holder.courseText.setText("Opleiding X");

        } else {

            holder.line.setVisibility(View.VISIBLE);
            holder.ownerTypeText.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);

            holder.ownerTypeText.setText(owner.getTypeName());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View v, categoryColor, line;
        public CardView cardView;
        public TextView nameText, courseText, ownerTypeText;
        public ClickListener clickListener;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);
            this.v = v;
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(getAdapterPosition(), true);
            return true;
        }
    }
}
