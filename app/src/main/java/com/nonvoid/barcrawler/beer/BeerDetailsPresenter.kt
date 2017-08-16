package com.nonvoid.barcrawler.beer

import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.social.FireBaseSocialClient
import com.nonvoid.barcrawler.social.SocialRepoAPI

class BeerDetailsPresenter(
        private val view: BeerDetailsView,
        private val beer: Beer,
        private val socialClient: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)){

    fun onCreate(){
        view.displayBeer(beer)
        getRating()
        getLiked()
        getReviews()
    }

    fun getRating(){
        socialClient.getBeerRating(beer)
                .subscribe({ result -> view.displayRating(result)})
    }

    fun getLiked(){
        socialClient.isBeerLiked(beer)
                .subscribe({ result -> view.displayLikeButtons(result)})
    }

    fun getReviews(){
        socialClient.getReviews(beer)
                .subscribe({ result -> view.displayBeerReviews(result)})
    }

    fun likeButtonPressed(){
        likeBeer(true)
    }

    fun dislikeButtonPressed(){
        likeBeer(false)
    }

    private fun likeBeer(like: Boolean){
        if(like){
            socialClient.likeBeer(beer)
        }else {
            socialClient.dislikeBeer(beer)
        }
        view.displayLikeButtons(like)
        getRating()
    }

    fun submitReview(message: String){
        socialClient.submitReview(beer, message)
    }

    interface BeerDetailsView{
        fun displayBeer(beer: Beer)
        fun displayLikeButtons(like :Boolean)
        fun displayRating(rating: Double)
        fun displayBeerReviews(reviews: List<String>)
    }
}