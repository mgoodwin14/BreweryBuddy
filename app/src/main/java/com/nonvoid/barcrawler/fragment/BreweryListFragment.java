package com.nonvoid.barcrawler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.adapter.BreweryAdapter;
import com.nonvoid.barcrawler.adapter.BreweryLocationListAdapter;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment implements BreweryLocationListAdapter.Callback, BreweryAdapter.Callback {

    private static final String TAG = BreweryListFragment.class.getSimpleName();
    private static final String BREWERY_LOCTION_LIST_BUNDLE_KEY = "brewery_locations_key";
    private static final String BREWERY_LIST_BUNDLE_KEY = "brewery_key";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;

    RecyclerView.Adapter adapter;

    @Inject
    BreweryAPI client;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();



    public static BreweryListFragment newInstance(List<BreweryLocation> locations){
        BreweryListFragment fragment = new BreweryListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY, (ArrayList<? extends Parcelable>) locations);
        fragment.setArguments(args);
        return fragment;
    }


    public static BreweryListFragment newInstance(ArrayList<Brewery> breweries) {
        BreweryListFragment fragment = new BreweryListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BREWERY_LIST_BUNDLE_KEY, breweries);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MPG onCreate");
        ((MyApp) getActivity().getApplication()).getNetComponent().inject(this);
        Bundle bundle = getArguments();
        if(bundle!= null) {
            ArrayList<Brewery> breweryList = bundle.getParcelableArrayList(BREWERY_LIST_BUNDLE_KEY);
            if(breweryList != null){
                adapter = new BreweryAdapter(breweryList, this);
            }else {

                List<BreweryLocation> breweryLocations = bundle.getParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY);
                if (breweryLocations != null) {
                    adapter = new BreweryLocationListAdapter(breweryLocations, this);
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);

        breweryListRecyclerView.setHasFixedSize(true);
        breweryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        breweryListRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
    }

    @Override
    public void onBrewerySelected(BreweryLocation location) {

        startActivity(BreweryDetailsActivity.newIntent(getContext(), location));
    }

    @Override
    public void onBrewerySelected(Brewery brewery, ImageView imageView) {
        Intent intent = BreweryDetailsActivity.newIntent(getContext(), brewery, imageView);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }
}
