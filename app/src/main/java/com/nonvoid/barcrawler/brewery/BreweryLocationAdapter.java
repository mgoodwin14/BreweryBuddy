package com.nonvoid.barcrawler.brewery;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/3/2017.
 */

public class BreweryLocationAdapter extends RecyclerView.Adapter<BreweryLocationAdapter.BreweryLocationViewHolder> {


    private final Callback callback;

    private List<BreweryLocation> breweryLocations;

    public BreweryLocationAdapter(List<BreweryLocation> breweryLocations, Callback callback) {
        this.breweryLocations = breweryLocations;
        this.callback = callback;
    }

    @Override
    public BreweryLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brewery_location_list_row, parent, false);
        view.setOnClickListener(v -> {
            int position = ((RecyclerView) v.getParent()).getChildLayoutPosition(v);
            callback.onBrewerySelected(breweryLocations.get(position));
        });
        return new BreweryLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BreweryLocationViewHolder holder, int position) {
        BreweryLocation location = breweryLocations.get(position);
        holder.setView(location);
        holder.itemView.setOnClickListener(view -> callback.onBrewerySelected(location));
        ViewCompat.setTransitionName(holder.imageView, location.getBrewery().getId());
    }

    @Override
    public int getItemCount() {
        return breweryLocations.size();
    }

    class BreweryLocationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.brewery_location_list_city_textview)
        TextView cityTextView;
        @BindView(R.id.brewery_list_name_textview)
        TextView nameTextView;
        @BindView(R.id.brewery_list_brand_classification_textview)
        TextView descriptionTextView;
        @BindView(R.id.brewery_location_image_view)
        ImageView imageView;


        BreweryLocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setView(BreweryLocation breweryLocation) {
            if(breweryLocation.getLocality()==null || breweryLocation.getRegion()==null){
                Toast.makeText(imageView.getContext(), "null location: " + breweryLocation.getName(), Toast.LENGTH_LONG).show();
            }

            cityTextView.setText(String.format("%s, %s", breweryLocation.getLocality(), breweryLocation.getRegion()));
            nameTextView.setText(breweryLocation.getBrewery().getName());
            descriptionTextView.setText(breweryLocation.getLocationTypeDisplay());
            if(breweryLocation.getBrewery().getImages() != null) {
                Picasso.with(imageView.getContext())
                        .load(breweryLocation.getBrewery().getImages().getLarge())
                    .into(imageView);
            }
        }
    }

    public interface Callback{
        void onBrewerySelected(BreweryLocation location);
    }
}
