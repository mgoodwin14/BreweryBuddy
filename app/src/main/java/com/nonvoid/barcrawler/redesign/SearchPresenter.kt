package com.nonvoid.barcrawler.redesign

import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import io.reactivex.Observable


class SearchPresenter(private val dbClient: BreweryDataBaseAPI) {


    fun searchBrewery(query: String): Observable<List<Brewery>> {
        setViewState(query, "brewery")
        return filter(query)
                .flatMap { dbClient.searchForBrewery(it) }
    }

    fun searchBeer(query: String): Observable<List<Beer>> {
        setViewState(query, "beer")
        return filter(query)
                .flatMap { dbClient.searchForBeer(query) }
    }


    fun searchLocation(query: String): Observable<List<BreweryLocation>> {
//        filter(query).doOnNext { setViewState(it, "location") }
        return filter(query)
                .flatMap { dbClient.searchCityForBreweries(query) }
    }

    private fun filter(query: String) = Observable.just(query).filter { it.length > 2 }

    companion object {

        var currentFilter = "brewery"
        var currentQuery = ""

        fun setViewState(query: String, filter: String) {
            currentFilter = filter
            currentQuery = query
        }
    }
}