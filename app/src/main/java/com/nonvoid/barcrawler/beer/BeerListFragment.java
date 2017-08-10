package com.nonvoid.barcrawler.beer;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.datalayer.io.BreweryDataBaseAPI;
import com.nonvoid.barcrawler.SearchFragment;
import com.nonvoid.barcrawler.model.Beer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Matt on 5/30/2017.
 */

public class BeerListFragment extends Fragment implements BeerAdapter.Callback, SearchFragment.Searchable {

    private static final String BEER_LIST_BUNDLE_KEY = "beer_list_key";
    private static final String BREWERY_ID_BUNDLE_KEY = "brewery_id_key";

    @Inject
    BreweryDataBaseAPI client;

    @BindView(R.id.brewery_list_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.search_empty_state)
    TextView emptyStateTextView;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Beer> beerList;
    private ProgressDialog loadingDialog;


    public static BeerListFragment newInstance(ArrayList<Beer> list) {
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BEER_LIST_BUNDLE_KEY, list);
        fragment.setArguments(args);
        return fragment;
    }

    public static BeerListFragment newInstance(String breweryId) {
        BeerListFragment fragment = new BeerListFragment();
        Bundle args = new Bundle();
        args.putString(BREWERY_ID_BUNDLE_KEY, breweryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
            beerList.sort((o1, o2) -> o1.getStyle().getShortName().compareTo(o2.getStyle().getShortName()));
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
        View view = inflater.inflate(R.layout.brewery_list_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        emptyStateTextView.setText("search for beers");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null && getArguments().getString(BREWERY_ID_BUNDLE_KEY) != null){
            emptyStateTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MyApp) getActivity(). getApplication()).getNetComponent().inject(this);
        Bundle bundle = getArguments();
        if(bundle != null) {
            beerList = bundle.getParcelableArrayList(BEER_LIST_BUNDLE_KEY);
            if(beerList!= null){
                recyclerView.setAdapter(new BeerAdapter(beerList, this));
            }else {
                String breweryId = bundle.getString(BREWERY_ID_BUNDLE_KEY);
                ProgressDialog dialog = ProgressDialog.show(context, "",
                        "Loading. Please wait...", true, true);

                Disposable disposable = client.getBeersForBrewery(breweryId)
                        .subscribe(
                                list -> {
                                    beerList = list;
                                    recyclerView.setAdapter(new BeerAdapter(beerList, this));
                                    dialog.dismiss();
                                },
                                throwable ->{
                                    Log.e("", throwable.getMessage(), throwable);
                                    dialog.dismiss();
                                }
                        );
                compositeDisposable.add(disposable);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        compositeDisposable.clear();
    }

    @Override
    public void onBeerSelected(Beer beer, ImageView imageView) {
        Intent intent = BeerDetailsActivity.Companion.newIntent(getContext(), beer, imageView);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                ViewCompat.getTransitionName(imageView));
        startActivity( intent, options.toBundle());
    }

    @Override
    public void doOnSearch(@NotNull String query) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        compositeDisposable.add(client.searchForBeer(query)
                .doOnSubscribe(x ->showLoading(true))
                .doOnComplete(() -> showLoading(false))
                .doOnError(throwable -> Log.d("MPG", throwable.getMessage(), throwable))
                .subscribe(this::setList));
    }

    private void setList(ArrayList<Beer> list) {
        if(list.isEmpty()){
            emptyStateTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            BeerAdapter adapter = new BeerAdapter(list, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        }

        beerList = list;
        recyclerView.setAdapter(new BeerAdapter(beerList, this));
    }


    private void showLoading(boolean show){
        if(show){
            loadingDialog = ProgressDialog.show(getContext(), "", "Searching for Breweries", false, true);
        }else {
            loadingDialog.dismiss();
        }
    }
}
