package com.nonvoid.barcrawler.beer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.social.BeerReviewAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.beer_details_activity.*

/**
 * Created by Matt on 6/30/2017.
 */
class BeerDetailsActivity : AppCompatActivity(), BeerDetailsPresenter.BeerDetailsView {

    lateinit var presenter: BeerDetailsPresenter

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beer_details_activity)
        (application as MyApp).netComponent.inject(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val beer :Beer = intent.extras.getParcelable(INTENT_BEER)
        presenter = BeerDetailsPresenter(this, beer)
        presenter.onCreate()

        submit_review_message_button.setOnClickListener{ presenter.submitReview(review_message_edit_text.text.toString())}
        like_button.setOnClickListener{ presenter.likeButtonPressed() }
        dislike_button.setOnClickListener{ presenter.dislikeButtonPressed() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayBeer(beer: Beer) {
        beer_details_name_textview.text = beer.name
        beer_details_description_textview.text = beer.description
        beer_details_style_text_view.text = beer.style.shortName
        beer_details_abv.text = "${beer.abv}% ABV"

        if(!beer.breweries.isEmpty()){
            beer_details_brewery_name.text = beer.breweries[0].nameShortDisplay
        }

        val picUrl :String?= beer.labels.large?:beer.labels.icon?:
                run{if(!beer.breweries.isEmpty()) beer.breweries[0]?.images?.large
                    else ""}

        beer_details_image_view.transitionName = intent.extras.getString(TRANSITION_NAME)

        if(picUrl != null && picUrl.isNotEmpty()) {
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
        }
    }

    override fun displayRating(rating: Double) {
        //rating is between 0 and 1
        if(rating > 0){
            beer_rating_bar.rating = rating.toFloat() * beer_rating_bar.numStars
        }else {
            Snackbar.make(beer_details_image_view, "Be the first to rate this beer", Snackbar.LENGTH_LONG).show()
            beer_rating_bar.visibility = View.GONE
        }
    }

    override fun displayLikeButtons(like: Boolean){
        if(like){
            like_button.setBackgroundColor(getColor(R.color.primary))
            dislike_button.setBackgroundColor(getColor(R.color.bright_foreground_disabled_material_dark))
        }else {
            like_button.setBackgroundColor(getColor(R.color.bright_foreground_disabled_material_dark))
            dislike_button.setBackgroundColor(getColor(R.color.primary))
        }
    }

    override fun displayBeerReviews(reviews: List<String>) {
        val adapter = BeerReviewAdapter(reviews)
        beer_reviews_recycler_view.layoutManager = LinearLayoutManager(this)
        beer_reviews_recycler_view.adapter = adapter
    }

    companion object {
        private const val INTENT_BEER = "beer"
        private const val TRANSITION_NAME = "beer_transition"

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
    }
}