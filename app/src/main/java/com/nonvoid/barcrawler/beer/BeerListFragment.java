package com.nonvoid.barcrawler.beer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.brewery.BreweryDetailsPresenter;
import com.nonvoid.barcrawler.model.Beer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Matt on 5/30/2017.
 */

public class BeerListFragment extends Fragment implements BeerAdapter.Callback, BreweryDetailsPresenter.BeerListView {

    private static final String BEER_LIST_BUNDLE_KEY = "beer_list_key";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.search_empty_state)
    ImageView emptyStateTextView;

    private BeerAdapter.Callback callback;

    public static BeerListFragment newInstance(ArrayList<Beer> list) {
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BEER_LIST_BUNDLE_KEY, list);
        fragment.setArguments(args);
        return fragment;
    }

    public static BeerListFragment newInstance(BeerAdapter.Callback callback){
        BeerListFragment fragment = new BeerListFragment();
        fragment.callback = callback;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
//        emptyStateTextView.setText("search for beers");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            ArrayList<Beer> beerList = bundle.getParcelableArrayList(BEER_LIST_BUNDLE_KEY);
            if(beerList!=null) {
                displayBeerList(beerList);
            }
        }
    }

    @Override

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        Menu sub = menu.addSubMenu("Sort");
//        sub.add("abc");
//        sub.add("type");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getTitle().toString().equalsIgnoreCase("type")){
//            beerList.sort((o1, o2) -> o1.getStyle().getShortName().compareTo(o2.getStyle().getShortName()));
//            recyclerView.getAdapter().notifyDataSetChanged();
//            return true;
//        }else if (item.getTitle().toString().equalsIgnoreCase("abc")){
//            beerList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
//            recyclerView.getAdapter().notifyDataSetChanged();
//            return true;
//        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBeerSelected(Beer beer, ImageView imageView) {
        if(callback!=null){
            callback.onBeerSelected(beer, imageView);
        }else {
            Intent intent = BeerDetailsActivity.Companion.newIntent(getContext(), beer, imageView);

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(),
                    imageView,
                    ViewCompat.getTransitionName(imageView));
            startActivity(intent, options.toBundle());
        }
    }

    @Override
    public void displayBeerList(@NotNull List<Beer> beerList) {
        Log.d("MPG", "displayBeerList");

        if(!beerList.isEmpty()){
            recyclerView.setAdapter(new BeerAdapter(beerList, this));
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        }else {
            emptyStateTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void display(Observable<List<Beer>> beerSearch) {

        Log.d("MPG", "display: BeerList");

        beerSearch.subscribe(new DisposableObserver<List<Beer>>() {
            @Override
            public void onNext(List<Beer> beers) {
                displayBeerList(beers);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
