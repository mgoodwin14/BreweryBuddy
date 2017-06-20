package com.nonvoid.barcrawler.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.dagger.MyApp;
import com.nonvoid.barcrawler.fragment.BreweryListFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
    //                    mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_dashboard:
    //                    mTextMessage.setText(R.string.title_dashboard);
                        return true;
                    case R.id.navigation_search:
                        fragment = new BreweryListFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_activity_content, fragment)
                                .commit();
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApp) getApplication()).getNetComponent().inject(this);


//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
