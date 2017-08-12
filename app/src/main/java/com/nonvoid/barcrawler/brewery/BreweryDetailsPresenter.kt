package com.nonvoid.barcrawler.brewery

import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.social.FireBaseSocialClient
import com.nonvoid.barcrawler.social.SocialRepoAPI
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer

class BreweryDetailsPresenter(
        private val view: BreweryDetailsView,
        private val brewery: Brewery,
        private val dbClient: BreweryDataBaseAPI,
        private val socialClient: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)
){

    fun onCreate(){
        view.displayBrewery(brewery)
        socialClient.getNumberOfFavoritesForBrewery(brewery)
                .subscribe({result -> view.displayFavoriteCount(result)})
    }

    fun setFavoriteMenuItem(){
        socialClient.isBreweryFavorited(brewery)
                .subscribe({result -> view.displayAsFavorite(result)})
    }

    fun toggleFavorite(){
        socialClient.isBreweryFavorited(brewery)
                .subscribe({result-> setBreweryAsFavorite(!result)})
    }

    fun getBeerList(){
        dbClient.getBeersForBrewery(brewery.id)
                .subscribe{list -> view.displayBeerList(list)}
    }

    private fun setBreweryAsFavorite(favorite: Boolean){
        if(favorite){
            socialClient.favoriteBrewery(brewery)
        }else {
            socialClient.unfavoriteBrewery(brewery)
        }
        view.displayAsFavorite(favorite)
        socialClient.getNumberOfFavoritesForBrewery(brewery)
                .subscribe({result -> view.displayFavoriteCount(result)})
    }

    interface BreweryDetailsView{
        fun displayBrewery(brewery :Brewery)
        fun displayBeerList(list: List<Beer>)
        fun displayAsFavorite(favorite :Boolean)
        fun displayFavoriteCount(count :Int)
    }
}