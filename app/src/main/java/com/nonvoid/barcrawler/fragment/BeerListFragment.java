package com.nonvoid.barcrawler.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.nonvoid.barcrawler.adapter.BeerAdapter;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.model.Beer;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Matt on 5/30/2017.
 */

public class BeerListFragment extends Fragment implements BeerAdapter.Callback {

    private static final String BEER_LIST_BUNDLE_KEY = "beer_list_key";
    private static final String BREWERY_ID_BUNDLE_KEY = "brewery_id_key";

    @Inject
    BreweryAPI client;

    @BindView(R.id.beer_list_recyclerview)
    RecyclerView recyclerView;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArrayList<Beer> beerList;


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
        View view = inflater.inflate(R.layout.beer_list_fragment, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        return view;
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
                        "Loading. Please wait...", true);

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
    public void onBeerSelected(Beer beer) {
        Toast.makeText(getContext(), "Selected: " +beer.getName(), Toast.LENGTH_LONG).show();
        startActivity( BeerDetailsActivity.Companion.newIntent(getContext(), beer ));
    }
}
