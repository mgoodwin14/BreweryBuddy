package com.nonvoid.barcrawler.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.datalayer.client.BreweryClient
import com.nonvoid.barcrawler.model.Beer
import kotlinx.android.synthetic.main.beer_details_activity.*

/**
 * Created by Matt on 6/30/2017.
 */
class BeerDetailsActivity : AppCompatActivity() {

    val client = BreweryClient()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beer_details_activity)

        val beer : Beer = intent.extras.getParcelable(INTENT_BEER_ID)
        beer_details_name_textview.text = beer.name
        beer_details_description_textview.text = beer.description

        client.getBeer(beer.id).subscribe(
                {beer -> doBeerStuff(beer)}
        )
    }

    private fun doBeerStuff(beer: Beer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private val INTENT_BEER_ID = "beer_id"

        fun newIntent(context: Context, beer: Beer): Intent {
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER_ID, beer)
            return intent
        }
    }
}