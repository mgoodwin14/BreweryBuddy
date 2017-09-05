package com.nonvoid.barcrawler.social

import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import io.reactivex.Observable

/**
 * Created by Matt on 8/20/2017.
 */
class ProfilePresenter(private val view: ProfileView,
                       private val dbClient: BreweryDataBaseAPI,
                       private val socialRepoAPI: SocialRepoAPI = FireBaseSocialClient(FirebaseAuth.getInstance().currentUser!!)){

    fun onCreate(){
        getFavoriteBreweries()
        getLikedBeers()
    }

    fun getFavoriteBreweries(){
        socialRepoAPI.getFavoriteBreweries()
                .flatMapObservable { list ->
                    if(!list.isEmpty()) {
                        dbClient.getBreweriesById(list)
                    } else {
                        Observable.empty()
                    }}
                .subscribe{list -> view.displayFavoriteBreweries(list) }
    }

    fun getLikedBeers(){
        socialRepoAPI.getLikedBeers()
                .flatMapObservable { list ->
                    if(!list.isEmpty()){
                        dbClient.getBeersById(list)
                    }else {
                        Observable.empty()
                    }
                }
    }

    interface ProfileView{
        fun displayFavoriteBreweries(breweryList: List<Brewery>)
        fun displayLikedBeers(beerList: List<Beer>)
    }
}