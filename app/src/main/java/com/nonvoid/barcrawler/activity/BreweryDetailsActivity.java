package com.nonvoid.barcrawler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.fragment.BreweryMapFragment;
import com.nonvoid.barcrawler.model.BreweryLocation;
import com.nonvoid.barcrawler.util.IntentTags;

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            BreweryLocation location = bundle.getParcelable(IntentTags.BREWERY_ITEM);
            if (location != null) {
                BreweryMapFragment fragment = BreweryMapFragment.newInstance(location);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.brewery_map_fragment_frame, fragment)
                        .commit();
            }
        }
    }
}
