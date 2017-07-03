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

public class BreweryLocationListAdapter extends RecyclerView.Adapter<BreweryLocationListAdapter.BreweryLocationViewHolder> {


    private final Callback callback;

    private List<BreweryLocation> breweryLocations;

    public BreweryLocationListAdapter(List<BreweryLocation> breweryLocations, Callback callback) {
        this.breweryLocations = breweryLocations;
        this.callback = callback;
    }

    @Override
    public BreweryLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brewery_list_row, parent, false);
        view.setOnClickListener(v -> {
            int position = ((RecyclerView) v.getParent()).getChildLayoutPosition(v);
            callback.onBrewerySelected(breweryLocations.get(position));
        });
        return new BreweryLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BreweryLocationViewHolder holder, int position) {
        holder.setView(breweryLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return breweryLocations.size();
    }

    class BreweryLocationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.brewery_list_name_textview)
        TextView nameTextView;
        @BindView(R.id.brewery_list_description_textview)
        TextView descriptionTextView;

        BreweryLocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setView(BreweryLocation breweryLocation) {
            nameTextView.setText(breweryLocation.getName());
            descriptionTextView.setText(breweryLocation.getLocationType());
        }
    }

    public interface Callback{
        void onBrewerySelected(BreweryLocation location);
    }
}