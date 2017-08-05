package com.nonvoid.barcrawler.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.activity.BreweryDetailsActivity;
import com.nonvoid.barcrawler.adapter.BreweryAdapter;
import com.nonvoid.barcrawler.adapter.BreweryLocationAdapter;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.model.Brewery;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryListFragment extends Fragment implements BreweryAdapter.Callback, SearchFragment.Searchable {

    private static final String TAG = BreweryListFragment.class.getSimpleName();
    private static final String BREWERY_LIST_BUNDLE_KEY = "brewery_key";

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView breweryListRecyclerView;
    @BindView(R.id.search_empty_state)
    TextView emptyStateTextView;

    @Inject
    BreweryAPI client;

    private ProgressDialog loadingDialog;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MPG onCreate");
        ((MyApp) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);
        breweryListRecyclerView.setHasFixedSize(true);
        breweryListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyStateTextView.setText("search for brewery");
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.clear();
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

    @Override
    public void doOnSearch(@NotNull String query) {
        Log.d("MPG", "Searching brewery list");
        //hide keyboard
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        compositeDisposable.add(client.searchForBrewery(query)
                .doOnSubscribe( x ->showLoading(true))
                .doOnComplete( () ->showLoading(false))
                .doOnError(throwable -> Log.d("MPG", throwable.getMessage(), throwable))
                .subscribe(this::setList));
    }

    private void setList(ArrayList<Brewery> list) {
        if(list.isEmpty()){
            emptyStateTextView.setVisibility(View.VISIBLE);
            breweryListRecyclerView.setVisibility(View.GONE);
        }else {
            BreweryAdapter adapter = new BreweryAdapter(list, this);
            breweryListRecyclerView.setAdapter(adapter);
            breweryListRecyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        }
    }

    private void showLoading(boolean show){
        if(show){
            loadingDialog = ProgressDialog.show(getContext(), "", "Searching for Breweries", false, true);
        }else {
            loadingDialog.dismiss();
        }
    }
}
