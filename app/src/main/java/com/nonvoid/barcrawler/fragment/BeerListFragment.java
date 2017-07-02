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
import com.nonvoid.barcrawler.model.Brewery;
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

    private static final String BEER_LIST_BUNDLE_KEY = "beer_list_key";
    private static final String BREWERY_ID_BUNDLE_KEY = "brewery_id_key";

    @Inject
    BreweryAPI client;

    @BindView(R.id.beer_list_recyclerview)
    RecyclerView recyclerView;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Beer> beerList;

    @org.jetbrains.annotations.Nullable
    public static BeerListFragment newInstance(@org.jetbrains.annotations.Nullable ArrayList<Beer> list) {
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BEER_LIST_BUNDLE_KEY, list);
        fragment.setArguments(args);
        return fragment;
    }

    public static BeerListFragment newInstance(@org.jetbrains.annotations.Nullable String breweryId) {
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putString(BREWERY_ID_BUNDLE_KEY, breweryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if(bundle != null) {
            beerList = bundle.getParcelableArrayList(BEER_LIST_BUNDLE_KEY);
            if(beerList!= null){
                recyclerView.setAdapter(new BeerListAdapter(beerList, this));
            }else {
                String breweryId = bundle.getString(BREWERY_ID_BUNDLE_KEY);
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);

                Disposable disposable = client.getBeersForBrewery(breweryId)
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onBeerSelected(Beer beer) {
        Toast.makeText(getContext(), "Selected: " +beer.getName(), Toast.LENGTH_LONG).show();
        startActivity( BeerDetailsActivity.Companion.newIntent(getContext(), beer ) );
    }
}
