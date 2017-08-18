package com.nonvoid.barcrawler.beer;

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

            nameTextView.setText(beer.getName());

            String label = getLabel(beer);
            if(label !=null){
                Picasso.with(imageView.getContext())
                        .load(label)
                        .into(imageView);
            }

            String breweryName = getBreweryName(beer);
            if(breweryName != null && !breweryName.isEmpty()){
                breweryNameTextView.setText(breweryName);
            } else {
                breweryNameTextView.setVisibility(View.GONE);
            }

            if(beer.getStyle()!=null) {
                descriptionTextView.setText(beer.getStyle().getShortName());
            } else {
                descriptionTextView.setVisibility(View.GONE);
            }
        }

        private String getLabel(Beer beer){
            if(beer.getLabels() != null && beer.getLabels().getLarge()!=null){
                return beer.getLabels().getLarge();
            }
            if(!beer.getBreweries().isEmpty()
                    && beer.getBreweries().get(0).getImages() != null
                    && beer.getBreweries().get(0).getImages().getIcon() != null){
                return beer.getBreweries().get(0).getImages().getIcon();
            }
            return null;
        }

        private String getBreweryName(Beer beer){
            if(!beer.getBreweries().isEmpty()) {
                return beer.getBreweries().get(0).getNameShortDisplay();
            }
            return null;
        }
    }

    public interface Callback{
        void onBeerSelected(Beer beer, ImageView imageView);
    }
}
