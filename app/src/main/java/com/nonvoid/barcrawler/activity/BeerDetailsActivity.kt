package com.nonvoid.barcrawler.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI
import com.nonvoid.barcrawler.datalayer.api.RatingRepoAPI
import com.nonvoid.barcrawler.datalayer.client.FireBaseClient
import com.nonvoid.barcrawler.model.Beer
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.beer_details_activity.*
import javax.inject.Inject

/**
 * Created by Matt on 6/30/2017.
 */
class BeerDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var client :BreweryAPI

    lateinit var beer :Beer
    lateinit var ratingClient: RatingRepoAPI

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beer_details_activity)
        (application as MyApp).netComponent.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        beer = intent.extras.getParcelable(INTENT_BEER)
        beer_details_name_textview.text = beer.name
        beer_details_description_textview.text = beer.description
        beer_details_style_text_view.text = beer.style.shortName
        beer_details_abv.text = "${beer.abv}% ABV"

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
        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            ratingClient = FireBaseClient(user)

            getRating()

            ratingClient.isBeerLiked(beer).subscribe(
                    {result -> toggleLikeButtons(result) },
                    {throwable -> Log.d("MPG", throwable.message, throwable) }
            )
            like_button.setOnClickListener{ toggleLikeButtons(true) }
            dislike_button.setOnClickListener{ toggleLikeButtons(false) }
        }
    }

    private fun getRating(){
        ratingClient.getBeerRating(beer).subscribe(
                {result ->
                    val rating = (result*100).toInt()
                    if(rating > 0){
                        beer_rating_text_view.text = "${rating}%"
                    }else {
                        beer_rating_text_view.text = "Be the first to like this beer"
                    }},
                {throwable -> Log.d("MPG", throwable.message, throwable) }
        )
    }

    private fun toggleLikeButtons(like: Boolean){
        if(like){
            ratingClient.likeBeer(beer)
            like_button.setBackgroundColor(getColor(R.color.primary))
            dislike_button.setBackgroundColor(getColor(R.color.bright_foreground_disabled_material_dark))
        }else {
            ratingClient.dislikeBeer(beer)
            like_button.setBackgroundColor(getColor(R.color.bright_foreground_disabled_material_dark))
            dislike_button.setBackgroundColor(getColor(R.color.primary))
        }
        getRating()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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