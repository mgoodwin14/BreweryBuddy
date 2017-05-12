package com.nonvoid.barcrawler.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BreweryListActivity;
import com.nonvoid.barcrawler.adapter.BreweryListAdapter;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment {

    private static final String TAG = BreweryListActivity.class.getSimpleName();
    private static final String BREWERY_LOCTION_LIST_BUNDLE_KEY = "brewery_locations";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;

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
        List<BreweryLocation> args = getArguments().getParcelableArrayList(BREWERY_LOCTION_LIST_BUNDLE_KEY);

        if(args!=null) {
            breweryListRecyclerView.setAdapter(new BreweryListAdapter(args));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "MPG onResume");

        breweryListRecyclerView.getAdapter().notifyDataSetChanged();
    }
}
