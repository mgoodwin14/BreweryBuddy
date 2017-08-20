package com.nonvoid.barcrawler.social

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery

/**
 * Created by Matt on 8/20/2017.
 */
class ProfilePresenter(private val view: ProfileView,
                       private val dbClient: BreweryDataBaseAPI,
                       private val socialRepoAPI: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)){

    fun getFavoriteBreweries(){
        socialRepoAPI.getFavoriteBreweries()
                .flatMapObservable { list -> dbClient.getBreweriesById(list) }
                .subscribe{list -> view.displayFavoriteBreweries(list) }
    }

    interface ProfileView{
        fun displayFavoriteBreweries(breweryList: List<Brewery>)
        fun displayLikedBeers(beerList: List<Beer>)
    }
}