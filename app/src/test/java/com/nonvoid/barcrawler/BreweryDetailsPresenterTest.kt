package com.nonvoid.barcrawler

import com.nonvoid.barcrawler.brewery.BreweryDetailsPresenter
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.social.SocialRepoAPI
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BreweryDetailsPresenterTest {
    val mockBrewery = Brewery()
    val mockBeerList = mutableListOf(Beer())
    val detailsView = Mockito.mock(BreweryDetailsPresenter.BreweryDetailsView::class.java)!!
    val listView = Mockito.mock(BreweryDetailsPresenter.BeerListView::class.java)
    val dbClient = Mockito.mock(BreweryDataBaseAPI::class.java)!!
    val socialClient = Mockito.mock(SocialRepoAPI::class.java)!!

    val subject = BreweryDetailsPresenter(detailsView, listView, mockBrewery, dbClient,  socialClient)

    @Before
    fun setUp(){
        Mockito.`when`(socialClient.isBreweryFavorited(mockBrewery))
                .thenReturn(Single.just(true))
        Mockito.`when`(socialClient.getNumberOfFavoritesForBrewery(mockBrewery))
                .thenReturn(Single.just(1))
        Mockito.`when`(dbClient.getBeersForBrewery(mockBrewery.id))
                .thenReturn(Observable.just(mockBeerList))

        Mockito.doNothing().`when`(socialClient).favoriteBrewery(mockBrewery)
        Mockito.doNothing().`when`(socialClient).unfavoriteBrewery(mockBrewery)
    }

    @Test
    fun onCreate_success(){
        subject.onCreate()
        Mockito.verify(detailsView).displayBrewery(mockBrewery)
        Mockito.verify(socialClient).getNumberOfFavoritesForBrewery(mockBrewery)
    }

    @Test
    fun setFavoriteMenuItem_success(){
        subject.setFavoriteMenuItem()
        Mockito.verify(socialClient).isBreweryFavorited(mockBrewery)
        Mockito.verify(detailsView).displayAsFavorite(true)
    }

    @Test
    fun toggleFavorite_success(){
        subject.toggleFavorite()
        Mockito.verify(socialClient).isBreweryFavorited(mockBrewery)
        Mockito.verify(detailsView).displayAsFavorite(false)
        Mockito.verify(socialClient).getNumberOfFavoritesForBrewery(mockBrewery)
        Mockito.verify(detailsView).displayFavoriteCount(1)

    }

    @Test
    fun getBeerList_success(){
        subject.getBeerList()
        Mockito.verify(dbClient).getBeersForBrewery(mockBrewery.id)
        Mockito.verify(listView).displayBeerList(mockBeerList)
    }
}