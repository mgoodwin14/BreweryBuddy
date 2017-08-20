package com.nonvoid.barcrawler.social

import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery

/**
 * Created by Matt on 8/20/2017.
 */
class ProfilePresenter(private val view: ProfileView,
                       private val dbClient: BreweryDataBaseAPI,
                       private val socialRepoAPI: SocialRepoAPI){

    fun getFavoriteBreweries(){
//        socialRepoAPI.getFavoriteBreweries()
    }

    interface ProfileView{
        fun displayFavoriteBreweries(breweryList: List<Brewery>)
        fun displayLikedBeers(beerList: List<Beer>)
    }
}