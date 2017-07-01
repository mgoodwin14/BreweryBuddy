package com.nonvoid.barcrawler.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.model.Beer

/**
 * Created by Matt on 6/30/2017.
 */
class BeerDetailsActivity : AppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beer_details_activity)

        intent.extras.get(INTENT_BEER_ID)
    }

    companion object {

        private val INTENT_BEER_ID = "beer_id"

        fun newIntent(context: Context, beer: Beer): Intent {
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER_ID, beer.id)
            return intent
        }
    }
}