package com.nonvoid.barcrawler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.fragment.BreweryMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/13/2017.
 */

public class BreweryDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brewery_details_activity);
        ButterKnife.bind(this);

        BreweryMapFragment fragment = new BreweryMapFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.brewery_map_fragment_frame, fragment)
                .commit();
    }
}
