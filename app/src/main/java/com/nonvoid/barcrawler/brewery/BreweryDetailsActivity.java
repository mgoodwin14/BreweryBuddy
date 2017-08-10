package com.nonvoid.barcrawler.brewery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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
import com.google.firebase.auth.FirebaseUser;
import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.social.SocialRepoAPI;
import com.nonvoid.barcrawler.social.FireBaseSocialClient;
import com.nonvoid.barcrawler.beer.BeerListFragment;
import com.nonvoid.barcrawler.location.BreweryMapFragment;
import com.nonvoid.barcrawler.model.Brewery;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/13/2017.
 */

public class BreweryDetailsActivity extends AppCompatActivity {

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
    @BindView(R.id.beer_list_button)
    Button beerListButton;
    @BindView(R.id.map_button)
    Button mapButton;
    @BindView(R.id.brewery_description_button)
    Button descriptionButton;
    @BindView(R.id.brewery_beer_list_fragment_frame)
    FrameLayout beerListFragmentFrame;
    @BindView(R.id.brewery_map_fragment_frame)
    FrameLayout mapFragmentFrame;


//    private String breweryId;
    private Brewery brewery;

    @Inject
    SharedPreferences sharedPref;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SocialRepoAPI ratingClient = new FireBaseSocialClient(user);


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
                breweryDescriptionTextView.setText(location.getDescription());
                breweryDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

                BreweryMapFragment fragment = BreweryMapFragment.newInstance(location);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.brewery_map_fragment_frame, fragment)
                        .addToBackStack(null)
                        .commit();

//                breweryId = location.getBreweryId();
                brewery = location.getBrewery();
            } else {
                brewery = bundle.getParcelable(BREWERY_ITEM);
                if(brewery!= null){
                    breweryNameTextView.setText(brewery.getName());
                    breweryDescriptionTextView.setText(brewery.getDescription());
//                    breweryId = brewery.getId();
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.brewery_beer_list_fragment_frame, BeerListFragment.newInstance(brewery.getId()))
                            .commit();

                    if(!brewery.getBreweryLocations().isEmpty()) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.brewery_map_fragment_frame, BreweryMapFragment.newInstance(brewery.getBreweryLocations().get(0)))
                                .commit();
                    }

//                    beerListButton.setVisibility(View.GONE);
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
            }
        }
        setFravoriteCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.brewery_details_menu, menu);
        if(sharedPref.getBoolean(brewery.getId(), false)) {
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


    public void onClick(View view){
        switch (view.getId()){
            case R.id.beer_list_button:
                beerListButton.setVisibility(View.GONE);
                descriptionButton.setVisibility(View.VISIBLE);
                mapButton.setVisibility(View.VISIBLE);

                beerListFragmentFrame.setVisibility(View.VISIBLE);
                breweryDescriptionTextView.setVisibility(View.GONE);
                mapFragmentFrame.setVisibility(View.GONE);
                break;

            case R.id.map_button:
                mapButton.setVisibility(View.GONE);
                beerListButton.setVisibility(View.VISIBLE);
                descriptionButton.setVisibility(View.VISIBLE);

                mapFragmentFrame.setVisibility(View.VISIBLE);
                beerListFragmentFrame.setVisibility(View.GONE);
                breweryDescriptionTextView.setVisibility(View.GONE);
                break;
            case R.id.brewery_description_button:
                descriptionButton.setVisibility(View.GONE);
                mapButton.setVisibility(View.VISIBLE);
                beerListButton.setVisibility(View.VISIBLE);

                breweryDescriptionTextView.setVisibility(View.VISIBLE);
                mapFragmentFrame.setVisibility(View.GONE);
                beerListFragmentFrame.setVisibility(View.GONE);
                break;
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

        Boolean fav = !sharedPref.getBoolean(brewery.getId(), false);
        sharedPref.edit()
                .putBoolean(brewery.getId(), fav)
                .apply();

        ratingClient.setBreweryAsFavorite(brewery, fav);
        setFravoriteCount();

        invalidateOptionsMenu();
    }

    private void setFravoriteCount() {
        ratingClient.getNumberOfFavoritesForBrewery(brewery)
                .subscribe(numberOfFavorites -> {
                    Log.d("MPG", "got favorite is not null");
                    if(numberOfFavorites==null || numberOfFavorites==0){
                        numberOfFavoritesTextView.setText("Be the first to favorite this brewery!");
                    }
                    else if(numberOfFavorites==1){
                        numberOfFavoritesTextView.setText("1 person has favorited this brewery");
                    }
                    else{
                        numberOfFavoritesTextView.setText(numberOfFavorites + " people have favorited this brewery");
                    }
                }, throwable -> Log.d("MPG", throwable.getMessage(), throwable)
            );
    }
}
