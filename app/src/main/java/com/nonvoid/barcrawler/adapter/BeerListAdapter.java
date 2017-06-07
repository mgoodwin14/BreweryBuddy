package com.nonvoid.barcrawler.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.Beer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerListViewHolder> {

    private List<Beer> beerList;

    public BeerListAdapter(List<Beer> beerList) {
        this.beerList = beerList;
    }

    @Override
    public BeerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_list_row, parent, false);
//        view.setOnClickListener(onClickListener);
        BeerListViewHolder viewHolder = new BeerListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BeerListViewHolder holder, int position) {
        holder.setView(beerList.get(position));
    }

    @Override
    public int getItemCount() {
        return beerList.size();
    }

    class BeerListViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.beer_list_name_textview)
        TextView nameTextView;
        @BindView(R.id.beer_list_style_textview)
        TextView descriptionTextView;

        public BeerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setView(Beer beer) {
            nameTextView.setText(beer.getName());
            descriptionTextView.setText(beer.getDescription());
        }
    }
}
