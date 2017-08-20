package com.nonvoid.barcrawler.brewery

import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.social.FireBaseSocialClient
import com.nonvoid.barcrawler.social.SocialRepoAPI
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.database.BreweryDataBaseClient
import com.nonvoid.barcrawler.model.Beer

class BreweryDetailsPresenter(
        private val detailsView: BreweryDetailsView,
        private val listView: BeerListView,
        private val brewery: Brewery,
        private val dbClient: BreweryDataBaseAPI,
        private val socialClient: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)
){

    fun onCreate(){
        detailsView.displayBrewery(brewery)
        getBeerList()
        socialClient.getNumberOfFavoritesForBrewery(brewery)
                .subscribe({result -> detailsView.displayFavoriteCount(result)})
    }

    fun getBrewery(): Brewery{
        return brewery
    }

    fun setFavoriteMenuItem(){
        socialClient.isBreweryFavorited(brewery)
                .subscribe({result -> detailsView.displayAsFavorite(result)})
    }

    fun toggleFavorite(){
        socialClient.isBreweryFavorited(brewery)
                .subscribe({result-> setBreweryAsFavorite(!result)})
    }

    fun getBeerList(){
        dbClient.getBeersForBrewery(brewery.id)
                .subscribe{list -> listView.displayBeerList(list)}
    }

    fun submitComment(message: String){
        socialClient.submitComment(brewery, message)
    }

    private fun setBreweryAsFavorite(favorite: Boolean){
        if(favorite){
            socialClient.favoriteBrewery(brewery)
        }else {
            socialClient.unfavoriteBrewery(brewery)
        }
        detailsView.displayAsFavorite(favorite)
        socialClient.getNumberOfFavoritesForBrewery(brewery)
                .subscribe({result -> detailsView.displayFavoriteCount(result)})
    }

    interface BreweryDetailsView{
        fun displayBrewery(brewery :Brewery)
        fun displayAsFavorite(favorite :Boolean)
        fun displayFavoriteCount(count :Int)
    }

    interface BeerListView{
        fun displayBeerList(list: MutableList<Beer>)
    }

    class Builder{
        private lateinit var detailsView: BreweryDetailsView
        private lateinit var listView: BeerListView
        private lateinit var brewery: Brewery
        private lateinit var dbClient: BreweryDataBaseAPI
        private lateinit var socialClient: SocialRepoAPI

        fun detailsView(view: BreweryDetailsView) :Builder{
            this.detailsView = view
            return this
        }

        fun listView(view: BeerListView) :Builder{
            this.listView = view
            return this
        }

        fun brewery(brewery: Brewery) :Builder{
            this.brewery = brewery
            return this
        }

        fun dbClient(dbClient: BreweryDataBaseAPI) :Builder{
            this.dbClient = dbClient
            return this
        }

        fun sociallClient(socialClient: SocialRepoAPI) :Builder{
            this.socialClient = socialClient
            return this
        }

        fun build(): BreweryDetailsPresenter{
            return BreweryDetailsPresenter(detailsView, listView, brewery, dbClient, socialClient)
        }
    }
}