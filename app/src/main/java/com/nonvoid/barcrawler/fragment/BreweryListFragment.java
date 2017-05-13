package com.nonvoid.barcrawler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.activity.BreweryListActivity;
import com.nonvoid.barcrawler.adapter.BreweryListAdapter;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.IntentTags;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment implements BreweryListAdapter.Callback {

    private static final String TAG = BreweryListActivity.class.getSimpleName();
    private static final String BREWERY_LOCTION_LIST_BUNDLE_KEY = "brewery_locations";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;

    private final List<BreweryLocation> breweryLocations = new ArrayList<>();

    public static BreweryListFragment newInstance(List<BreweryLocation> locations){
        BreweryListFragment fragment = new BreweryListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY, (ArrayList<? extends Parcelable>) locations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MPG onCreate");

        breweryLocations.addAll( getArguments().getParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY) );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);
        breweryListRecyclerView.setHasFixedSize(true);
        breweryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        breweryListRecyclerView.setAdapter(new BreweryListAdapter(breweryLocations, this));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MPG onResume");

        breweryListRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onBrewerySelected(BreweryLocation location) {
        Intent intent = new Intent(getContext(), BreweryDetailsActivity.class);
        intent.putExtra(IntentTags.BREWERY_ITEM, location);
        startActivity(intent);
    }
}
