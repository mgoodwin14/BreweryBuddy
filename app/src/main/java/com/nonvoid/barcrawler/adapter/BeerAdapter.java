package com.nonvoid.barcrawler.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.Beer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerListViewHolder> {

    private final Callback callback;
    private List<Beer> beerList;

    public BeerAdapter(List<Beer> beerList, Callback callback) {
        this.beerList = beerList;
        this.callback = callback;
    }

    @Override
    public BeerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_list_row, parent, false);
        return new BeerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeerListViewHolder holder, int position) {
        Beer beer = beerList.get(position);
        holder.setView(beerList.get(position));
        holder.itemView.setOnClickListener(v -> {
            callback.onBeerSelected(beer, holder.imageView);
        });
        ViewCompat.setTransitionName(holder.imageView, beer.getId());
    }

    @Override
    public int getItemCount() {
        return beerList.size();
    }

    class BeerListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.beer_image_view)
        ImageView imageView;
        @BindView(R.id.beer_list_name_textview)
        TextView nameTextView;
        @BindView(R.id.beer_list_brewery_name_text_view)
        TextView breweryNameTextView;
        @BindView(R.id.beer_list_style_textview)
        TextView descriptionTextView;

        BeerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setView(Beer beer) {

            if(beer.getLabels() != null && beer.getLabels().getIcon()!=null){
                Picasso.with(imageView.getContext())
                        .load(beer.getLabels().getLarge())
                        .into(imageView);
            } else {
                if(!beer.getBreweries().isEmpty()
                        && beer.getBreweries().get(0).getImages() != null
                        && beer.getBreweries().get(0).getImages().getIcon() != null){
                    Picasso.with(imageView.getContext())
                            .load(beer.getBreweries().get(0).getImages().getLarge())
                            .into(imageView);
                }
            }
            nameTextView.setText(beer.getName());

            if(!beer.getBreweries().isEmpty()) {
                String  line2 = beer.getBreweries().get(0).getNameShortDisplay();
                if (!beer.getBreweries().get(0).getBreweryLocations().isEmpty()
                        && beer.getBreweries().get(0).getBreweryLocations().get(0).getLocality() != null) {
                    line2 += " - " + beer.getBreweries().get(0).getBreweryLocations().get(0).getLocality();
                    breweryNameTextView.setText(line2);
                } else {
                    breweryNameTextView.setVisibility(View.GONE);
                }
            }
            if(beer.getStyle()!=null) {
                descriptionTextView.setText(beer.getStyle().getShortName());
            } else {
                descriptionTextView.setVisibility(View.GONE);
            }
        }
    }

    public interface Callback{
        void onBeerSelected(Beer beer, ImageView imageView);
    }
}
