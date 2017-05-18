package com.nonvoid.barcrawler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.nonvoid.barcrawler.fragment.BreweryListFragment;
import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Matt on 5/3/2017.
 */

public class BreweryListActivity extends BaseActivity {

    private static final String TAG = BreweryListActivity.class.getSimpleName();

    private BreweryAPI client;
    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MPG onCreate");

        setContentView(R.layout.brewery_list_activity);
        ButterKnife.bind(this);

        client = new BreweryClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.brewery_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.map_button:
                //show list on map

                return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MPG onResume");
        subscriptions.add(
                client.getLocationsInCity("Columbus")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayBreweries, this::handleThrowable));
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.clear();
    }

    private void displayBreweries(List<BreweryLocation> breweryLocations) {
        Log.d(TAG, "MPG handle response");

        BreweryListFragment fragment = BreweryListFragment.newInstance(breweryLocations);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.brewery_list_frame, fragment)
                .show(fragment)
                .commit();
    }

    private void handleThrowable(Throwable throwable) {
        Log.d(TAG, "MPG handle throwable: " +throwable.getMessage());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(throwable.getMessage());
//        builder.show();
    }
}
