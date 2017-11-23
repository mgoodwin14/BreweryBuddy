package com.nonvoid.barcrawler

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Observable
import io.reactivex.Single

class HomePresenter(private val dbClient: BreweryDataBaseAPI){

    private var view: HomeView? = null

    fun onViewCreated(view: HomeView){
        this.view = view
    }

    fun onViewDestroyed(){
        view = null
    }

    fun  brewerySearch(query: String): Observable<List<Brewery>> {
        return dbClient.searchForBrewery(query)
                .doOnError{ Log.d("MPG", "brewery search error", it)}
    }

    fun beerSearch(query: String): Observable<List<Beer>> {
        return dbClient.searchForBeer(query)
                .doOnError{ Log.d("MPG", "beer search error", it)}
    }

    fun locationSearch(query: String): Observable<List<BreweryLocation>> {
        return dbClient.searchCityForBreweries(query)
                .doOnError{ Log.d("MPG", "location search error", it)}
    }

    interface HomeView
}