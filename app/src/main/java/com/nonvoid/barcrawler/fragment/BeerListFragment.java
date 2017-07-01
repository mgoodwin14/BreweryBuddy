package com.nonvoid.barcrawler.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BeerDetailsActivity;
import com.nonvoid.barcrawler.adapter.BeerListAdapter;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.Comparator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Matt on 5/30/2017.
 */

public class BeerListFragment extends Fragment implements BeerListAdapter.Callback {

    private static final String BREWERY_BEER_LIST_BUNDLE_KEY = "brewery_location";

    @Inject
    BreweryAPI client;

    @BindView(R.id.beer_list_recyclerview)
    RecyclerView recyclerView;

    private BreweryLocation location;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Beer> beerList;

    public static BeerListFragment newInstance(BreweryLocation location){
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putParcelable(BREWERY_BEER_LIST_BUNDLE_KEY, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if(bundle != null) {
            location = getArguments().getParcelable(BREWERY_BEER_LIST_BUNDLE_KEY);
        }
        ((MyApp) getActivity(). getApplication()).getNetComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Menu sub = menu.addSubMenu("Sort");
        sub.add("abc");
        sub.add("type");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equalsIgnoreCase("type")){
            beerList.sort((o1, o2) -> o1.getShortName().compareTo(o2.getShortName()));
            recyclerView.getAdapter().notifyDataSetChanged();
            return true;
        }else if (item.getTitle().toString().equalsIgnoreCase("abc")){
            beerList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
            recyclerView.getAdapter().notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beer_list_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        if(location!=null) {
            getBeers(location);
        }


        return view;
    }

    private void getBeers(BreweryLocation location) {

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);

        Disposable disposable = client.getBeersForBrewery(location)
                .subscribe(
                        list -> {
                            beerList = list;
                            recyclerView.setAdapter(new BeerListAdapter(beerList, this));
                            dialog.dismiss();
                        },
                        throwable -> Log.e("", throwable.getMessage(), throwable)
                );
        compositeDisposable.add(disposable);
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    @Override
    public void onBeerSelected(Beer beer) {
        Toast.makeText(getContext(), "Selected: " +beer.getName(), Toast.LENGTH_LONG).show();
        startActivity( BeerDetailsActivity.Companion.newIntent(getContext(), beer ) );
    }

    private void handleThrowable(Throwable throwable) {
        Log.e("", throwable.getMessage(), throwable);
    }

    private void handleOnNext(ArrayList<Beer> list) {
        beerList = list;
//                            recyclerView.setAdapter(new BeerListAdapter(beerList, this));
//                            dialog.dismiss();
    }
}
