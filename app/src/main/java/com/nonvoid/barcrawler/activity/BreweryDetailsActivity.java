package com.nonvoid.barcrawler.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.fragment.BeerListFragment;
import com.nonvoid.barcrawler.fragment.BreweryMapFragment;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.StringUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Matt on 5/13/2017.
 */

public class BreweryDetailsActivity extends AppCompatActivity {

    private static final String BREWERY_ITEM = "brewery_item";
    private static final String LOCATION_ITEM = "location_item";


    @BindView(R.id.brewery_details_name_textview)
    TextView breweryNameTextView;
    @BindView(R.id.brewery_details_description_textview)
    TextView breweryDescriptionTextView;
    @BindView(R.id.beer_list_button)
    Button button;

    private String breweryId;

    @Inject
    SharedPreferences sharedPref;


    public static Intent newIntent(Context context, BreweryLocation location){
        Intent intent = new Intent(context, BreweryDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOCATION_ITEM, location);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntent(Context context, Brewery brewery){
        Intent intent = new Intent(context, BreweryDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BREWERY_ITEM, brewery);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_details_activity);
        ((MyApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

            BreweryLocation location = bundle.getParcelable(LOCATION_ITEM);

            if (location != null) {
                breweryNameTextView.setText(location.getName());
                if(StringUtils.isNotNullOrEmpty( location.getDescription() )) {
                    breweryDescriptionTextView.setText(location.getDescription());
                    breweryDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
                }

                BreweryMapFragment fragment = BreweryMapFragment.newInstance(location);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.brewery_map_fragment_frame, fragment)
                        .addToBackStack(null)
                        .commit();

                breweryId = location.getBreweryId();
            } else {
                Brewery brewery = bundle.getParcelable(BREWERY_ITEM);
                if(brewery!= null){
                    breweryNameTextView.setText(brewery.getName());
                    breweryDescriptionTextView.setText(brewery.getDescription());
                    breweryId = brewery.getId();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.brewery_map_fragment_frame, BeerListFragment.newInstance(breweryId))
                            .addToBackStack(null)
                            .commit();

                    button.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.brewery_details_menu, menu);
        if(sharedPref.getBoolean(breweryId, false)) {
            menu.getItem(0).setIcon(R.drawable.ic_favorite_checked);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite_button:
                toggleFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.beer_list_button)
    public void onShowBeerList(){
        if(button.getText().toString().equalsIgnoreCase("beers")){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.brewery_map_fragment_frame, BeerListFragment.newInstance(breweryId))
                    .addToBackStack(null)
                    .commit();

            button.setText("Map");
        }else {
            onBackPressed();
            button.setText("Beers");
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void toggleFavorite() {

        Boolean fav = sharedPref.getBoolean(breweryId, false);
        sharedPref.edit()
                .putBoolean(breweryId, !fav)
                .apply();

        invalidateOptionsMenu();
    }
}
