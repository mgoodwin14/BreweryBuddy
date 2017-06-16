package com.nonvoid.barcrawler.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.adapter.BeerListAdapter;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Matt on 5/30/2017.
 */

public class BreweryBeerListFragment extends Fragment implements BeerListAdapter.Callback {

    private static final String BREWERY_BEER_LIST_BUNDLE_KEY = "brewery_location";

    @BindView(R.id.beer_list_recyclerview)
    RecyclerView recyclerView;

    private BreweryLocation location;
    private BreweryAPI client = new BreweryClient();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Beer> beerList;

    public static BreweryBeerListFragment newInstance(BreweryLocation location){
        BreweryBeerListFragment fragment = new BreweryBeerListFragment();
        Bundle args = new Bundle();
        args.putParcelable(BREWERY_BEER_LIST_BUNDLE_KEY, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = getArguments().getParcelable(BREWERY_BEER_LIST_BUNDLE_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beer_list_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);

        Disposable disposable = client.getBeersForBrewery(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            beerList = list;
                            recyclerView.setAdapter(new BeerListAdapter(beerList, this));
                            dialog.dismiss();
                        }
                );
        compositeDisposable.add(disposable);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    @Override
    public void onBeerSelected(Beer beer) {
        Toast.makeText(getContext(), "Selected: " +beer.getName(), Toast.LENGTH_LONG).show();
    }
}
