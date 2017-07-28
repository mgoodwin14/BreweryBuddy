package com.nonvoid.barcrawler.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI
import com.nonvoid.barcrawler.model.Beer
import kotlinx.android.synthetic.main.beer_details_activity.*

/**
 * Created by Matt on 6/30/2017.
 */
class BeerDetailsActivity : AppCompatActivity() {

    lateinit var client :BreweryAPI

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beer_details_activity)

        (application as MyApp).netComponent.inject(this)

        val beer : Beer = intent.extras.getParcelable(INTENT_BEER_ID)
        beer_details_name_textview.text = beer.name
        beer_details_description_textview.text = beer.description

//        client.getBeer(beer.id).subscribe(
//                {beer -> doBeerStuff(beer)}
//        )
    }

    private fun doBeerStuff(beer: Beer) {
        Snackbar.make(beer_details_description_textview.rootView, "Do Beer Stuff", Snackbar.LENGTH_LONG).show()
    }

    companion object {

        private val INTENT_BEER_ID = "beer_id"
        private val INTENT_BEER = "beer"

        fun newIntent(context: Context, beer: Beer): Intent {
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER_ID, beer)
            return intent
        }

        fun newIntent(context: Context, beerId: String): Intent{
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER_ID, beerId)
            return intent
        }
    }
}