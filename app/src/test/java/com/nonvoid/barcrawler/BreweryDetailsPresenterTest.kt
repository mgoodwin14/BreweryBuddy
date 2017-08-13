package com.nonvoid.barcrawler

import com.nonvoid.barcrawler.brewery.BreweryDetailsPresenter
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.social.SocialRepoAPI
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by Matt on 8/12/2017.
 */
class BreweryDetailsPresenterTest {
    val view = Mockito.mock(BreweryDetailsPresenter.BreweryDetailsView::class.java)
    val brewery = Mockito.mock(Brewery::class.java)
    val breweryDBAPI = Mockito.mock(BreweryDataBaseAPI::class.java)
    val socialClient = Mockito.mock(SocialRepoAPI::class.java)
    val subject = BreweryDetailsPresenter(view, brewery,breweryDBAPI,  socialClient)

    @Before
    fun setUp(){
        Mockito.`when`(socialClient.isBreweryFavorited(brewery))
                .thenReturn(Single.just(true))
        Mockito.`when`(socialClient.getNumberOfFavoritesForBrewery(brewery))
                .thenReturn(Single.just(1))

        Mockito.doNothing().`when`(socialClient).favoriteBrewery(brewery)
        Mockito.doNothing().`when`(socialClient).unfavoriteBrewery(brewery)
    }

    @Test
    fun onCreate_success(){
        subject.onCreate()
        Mockito.verify(view).displayBrewery(brewery)
        Mockito.verify(socialClient).getNumberOfFavoritesForBrewery(brewery)
    }
}