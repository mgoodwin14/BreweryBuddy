package com.nonvoid.barcrawler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/3/2017.
 */

public class BreweryListAdapter extends RecyclerView.Adapter<BreweryListAdapter.BreweryListViewHolder> {

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView) v.getParent()).getChildLayoutPosition(v);
            callback.onBrewerySelected(breweryLocations.get(position));
        }
    };
    private final Callback callback;
    private List<BreweryLocation> breweryLocations;

    public BreweryListAdapter(List<BreweryLocation> breweryLocations, Callback callback) {
        this.breweryLocations = breweryLocations;
        this.callback = callback;
    }

    @Override
    public BreweryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brewery_list_row, parent, false);
        view.setOnClickListener(onClickListener);
        BreweryListViewHolder viewHolder = new BreweryListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BreweryListViewHolder holder, int position) {
        holder.setView(breweryLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return breweryLocations.size();
    }

    class BreweryListViewHolder extends RecyclerView.ViewHolder{

        private BreweryLocation location;

        @BindView(R.id.brewery_list_name_textview)
        TextView nameTextView;
        @BindView(R.id.brewery_list_description_textview)
        TextView descriptionTextView;

        BreweryListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onBrewerySelected(location);
                }
            });
        }

        void setView(BreweryLocation breweryLocation) {
            nameTextView.setText(breweryLocation.getName());
            descriptionTextView.setText(breweryLocation.getDescription());
        }
    }

    public interface Callback{
        void onBrewerySelected(BreweryLocation location);
    }


}
