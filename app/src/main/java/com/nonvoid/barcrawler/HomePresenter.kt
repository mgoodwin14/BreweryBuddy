package com.nonvoid.barcrawler

import android.app.ProgressDialog
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import durdinapps.rxfirebase2.RxFirebaseAuth

/**
 * Created by Matt on 8/12/2017.
 */
class HomePresenter(private val view: HomeView, private val dbClient: BreweryDataBaseAPI){

    fun onCreate(){
        logIn()
        view.addSearchFragment()
    }

    private fun logIn(){
        val firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser!=null){
            firebaseAuth.currentUser!!.reload()
            Log.d("MPG", "currentUser: ${firebaseAuth.currentUser}")
        }else {
            val dialog = view.showDialog("Please wait", "Attempting to log in")
            RxFirebaseAuth.signInAnonymously(firebaseAuth)
                    .doFinally { dialog.dismiss() }
                    .subscribe({ result ->
                        if (result.user != null) view.makeToast("Signed in anonymously")
                        else view.makeToast("Sign in failed")
                    })
        }
    }

    fun  brewerySearch(query: String) {
        val dialog = view.showDialog("Searching for breweries", "please wait")
        dbClient.searchForBrewery(query)
                .doFinally{dialog.dismiss()}
                .subscribe{result -> view.displayBreweryList(result)}
    }

    fun beerSearch(query: String){
        val dialog = view.showDialog("Searching for beers", "please wait")
        dbClient.searchForBeer(query)
                .doFinally{dialog.dismiss()}
                .subscribe{result -> view.displayBeerFragment(result)}
    }

    fun locationSearch(query: String){
        val dialog = view.showDialog("Searching by location", "please wait")
        dbClient.searchCityForBreweries(query)
                .doFinally{dialog.dismiss()}
                .toList()
                .subscribe{result -> view.displayLocationFragment(result)}
    }

    interface HomeView{
        fun showDialog(title: String, message: String) : ProgressDialog
        fun makeToast(message: String)
        fun addSearchFragment()
        fun displayBreweryList(breweryList: List<Brewery>)
        fun displayLocationFragment(locationList: List<BreweryLocation>)
        fun displayBeerFragment(beerList: List<Beer>)
    }
}