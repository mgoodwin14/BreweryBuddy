package com.nonvoid.barcrawler.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI
import com.nonvoid.barcrawler.model.Beer
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val beer : Beer = intent.extras.getParcelable(INTENT_BEER)
        beer_details_name_textview.text = beer.name
        beer_details_description_textview.text = beer.description
        beer_details_style_text_view.text = beer.style.shortName
        abv_text_view.text = "${beer.abv}%"
        beg_grav_text_view.text = beer.style.ogMin
        end_grav_text_view.text = beer.style.fgMin

        val picUrl = beer.labels.large?:beer.labels.icon?: beer.breweries[0]?.images?.large

        beer_details_image_view.transitionName = intent.extras.getString(TRANSITION_NAME)
        Picasso.with(this)
                .load(picUrl)
                .noFade()
                .into(beer_details_image_view, object : Callback {
                    override fun onSuccess() {
                        supportStartPostponedEnterTransition()
                    }

                    override fun onError() {
                        supportStartPostponedEnterTransition()
                    }
                })

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
        private val TRANSITION_NAME = "beer_transition"

        fun newIntent(context: Context, beer: Beer): Intent {
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER, beer)
            return intent
        }

        fun newIntent(context: Context, beer: Beer, imageView: ImageView): Intent{
            val intent = newIntent(context, beer)
            intent.putExtra(TRANSITION_NAME, ViewCompat.getTransitionName(imageView))
            return intent
        }

        fun newIntent(context: Context, beerId: String): Intent{
            val intent = Intent(context, BeerDetailsActivity::class.java)
            intent.putExtra(INTENT_BEER_ID, beerId)
            return intent
        }
    }
}