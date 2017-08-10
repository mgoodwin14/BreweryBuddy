package com.nonvoid.barcrawler.beer

import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.social.FireBaseSocialClient
import com.nonvoid.barcrawler.social.SocialRepoAPI

/**
 * Created by Matt on 8/10/2017.
 */
class BeerDetailsPresenter(private val view: BeerDetailsView, private val beer: Beer){
    val socialClient: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)

    init {
        onCreate()
    }

    private fun onCreate(){
        view.displayBeer(beer)
        getRating()
        getLiked()
    }

    fun getRating(){
        socialClient.getBeerRating(beer).subscribe({ result -> view.displayRating((result*100).toInt())})
    }

    fun getLiked(){
        socialClient.isBeerLiked(beer).subscribe({ result -> view.toggleLikeButtons(result)})
    }

    fun likeButtonClicked(like: Boolean){
        if(like){
            socialClient.likeBeer(beer)
        }else {
            socialClient.dislikeBeer(beer)
        }
        view.toggleLikeButtons(like)
        getRating()
    }

    interface BeerDetailsView{
        fun displayBeer(beer: Beer)
        fun toggleLikeButtons(like :Boolean)
        fun displayRating(rating: Int)
    }
}