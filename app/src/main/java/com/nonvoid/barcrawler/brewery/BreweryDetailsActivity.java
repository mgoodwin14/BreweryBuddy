package com.nonvoid.barcrawler.brewery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI;
import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.social.SocialRepoAPI;
import com.nonvoid.barcrawler.social.FireBaseSocialClient;
import com.nonvoid.barcrawler.beer.BeerListFragment;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreweryDetailsActivity extends AppCompatActivity implements BreweryDetailsPresenter.BreweryDetailsView {

    private static final String BREWERY_ITEM = "brewery_item";
    private static final String LOCATION_ITEM = "location_item";
    private static final String TRANSITION_NAME = "brewery_transition";

    @BindView(R.id.brewery_details_image_view)
    ImageView imageView;
    @BindView(R.id.brewery_details_number_of_favorites)
    TextView numberOfFavoritesTextView;
    @BindView(R.id.brewery_details_name_textview)
    TextView breweryNameTextView;
    @BindView(R.id.brewery_details_description_textview)
    TextView breweryDescriptionTextView;
    @BindView(R.id.brewery_beer_list_fragment_frame)
    FrameLayout beerListFragmentFrame;
    @BindView(R.id.brewery_map_fragment_frame)
    FrameLayout mapFragmentFrame;

    @Inject
    BreweryDataBaseAPI dbClient;

    private BreweryDetailsPresenter presenter;
    private MenuItem favoriteMenuItem;

    public static Intent newIntent(Context context, BreweryLocation location){
        Intent intent = new Intent(context, BreweryDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOCATION_ITEM, location);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntent(Context context, Brewery brewery, ImageView imageView){
        Intent intent = new Intent(context, BreweryDetailsActivity.class);
        intent.putExtra(BREWERY_ITEM, brewery);
        intent.putExtra(TRANSITION_NAME, ViewCompat.getTransitionName(imageView));
        return intent;
    }

    public static Brewery getBreweryFromBundle(Bundle bundle){
        if(bundle!=null){
            Brewery brewery = bundle.getParcelable(BREWERY_ITEM);
            if(brewery!= null){
                return brewery;
            }
            BreweryLocation location = bundle.getParcelable(LOCATION_ITEM);
            if (location != null) {
                return location.getBrewery();
            }
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_details_activity);
        ((MyApp) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        Brewery brewery = getBreweryFromBundle(getIntent().getExtras());
        if(brewery == null){
            Log.e("MPG", "Brewery was NULL in BreweryDetailsActivity, finishing the activity instead of continuing");
            finish();
            return;
        }

        SocialRepoAPI socialClient = new FireBaseSocialClient(FirebaseAuth.getInstance().getCurrentUser());

        BeerListFragment fragment = new BeerListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.brewery_beer_list_fragment_frame, fragment)
                .commit();

        presenter = new BreweryDetailsPresenter.Builder()
                .brewery(brewery)
                .detailsView(this)
                .listView(fragment)
                .dbClient(dbClient)
                .socielClient(socialClient)
                .build();

        presenter.onCreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.brewery_details_menu, menu);
        favoriteMenuItem = menu.getItem(0);
        presenter.setFavoriteMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite_button:
                presenter.toggleFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void displayBrewery(@NotNull Brewery brewery) {
        breweryNameTextView.setText(brewery.getName());
        breweryDescriptionTextView.setText(brewery.getDescription());
        presenter.getBeerList();
        if(!brewery.getBreweryLocations().isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.brewery_map_fragment_frame, BreweryMapFragment.newInstance(brewery.getBreweryLocations().get(0)))
                    .commit();
        }else{
            mapButton.setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        String trans = bundle.getString(TRANSITION_NAME);
        imageView.setTransitionName(trans);
        Picasso.with(this)
                .load(brewery.getImages().getLarge())
                .noFade()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void displayBeerList(@NotNull List<? extends Beer> list) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.brewery_beer_list_fragment_frame, BeerListFragment.newInstance(new ArrayList<>(list)))
                .commit();
    }

    @Override
    public void displayAsFavorite(boolean favorite) {
        if(favorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_checked);
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_unchecked);
        }
    }

    @Override
    public void displayFavoriteCount(int count) {
        if(count==0){
            numberOfFavoritesTextView.setText("Be the first to favorite this brewery!");
        }
        else if(count==1){
            numberOfFavoritesTextView.setText("1 person has favorited this brewery");
        }
        else{
            numberOfFavoritesTextView.setText(count + " people have favorited this brewery");
        }
    }
}
