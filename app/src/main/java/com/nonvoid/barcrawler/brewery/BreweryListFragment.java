package com.nonvoid.barcrawler.brewery;

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
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.Brewery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment implements BreweryAdapter.Callback {

    private static final String TAG = BreweryListFragment.class.getSimpleName();
    private static final String BREWERY_LIST_BUNDLE_KEY = "brewery_key";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;
    @BindView(R.id.search_empty_state)
    ImageView emptyStateTextView;

    public static BreweryListFragment newInstance(ArrayList<Brewery> breweryList){
        BreweryListFragment fragment = new BreweryListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BREWERY_LIST_BUNDLE_KEY,breweryList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);
        breweryListRecyclerView.setHasFixedSize(true);
        breweryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = getArguments();
        if(bundle!=null){
            List<Brewery> breweryList = bundle.getParcelableArrayList(BREWERY_LIST_BUNDLE_KEY);
            displayList(breweryList);
        }else {
//            emptyStateTextView.setText("search for brewery");
        }
        return view;
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

    private void displayList(List<Brewery> list) {
        Log.d("MPG", "displayList: Brewery");

        if(list != null && !list.isEmpty()){
            BreweryAdapter adapter = new BreweryAdapter(list, this);
            breweryListRecyclerView.setAdapter(adapter);
            breweryListRecyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.VISIBLE);
            breweryListRecyclerView.setVisibility(View.GONE);
        }
    }

    public void display( Observable<List<Brewery>> brewerySearch) {
        Log.d("MPG", "display: brewerylist");
        brewerySearch.subscribe(new DisposableObserver<List<Brewery>>() {
            @Override
            public void onNext(List<Brewery> breweries) {
                Log.d("MPG", "onNext: brewerylist.size: " +breweries.size());
                displayList(breweries);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MPG", "BreweryListFragment.onError: "+ e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("MPG", "onComplete: brewerylist");
            }
        });
    }
}
