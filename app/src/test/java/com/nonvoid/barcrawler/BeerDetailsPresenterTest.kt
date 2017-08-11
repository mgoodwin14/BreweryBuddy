package com.nonvoid.barcrawler

import com.nonvoid.barcrawler.beer.BeerDetailsPresenter
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.social.SocialRepoAPI
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BeerDetailsPresenterTest{

    val view = Mockito.mock(BeerDetailsPresenter.BeerDetailsView::class.java)
    val beer = Mockito.mock(Beer::class.java)
    val socialClient = Mockito.mock(SocialRepoAPI::class.java)
    val subject = BeerDetailsPresenter(view, beer, socialClient)

    @Before
    fun setUp(){
        Mockito.`when`(socialClient.getBeerRating(Mockito.any()))
                .thenReturn(Maybe.empty())
        Mockito.`when`(socialClient.isBeerLiked(Mockito.any()))
                .thenReturn(Maybe.just(true))
        Mockito.doNothing().`when`(socialClient).likeBeer(beer)
        Mockito.doNothing().`when`(socialClient).dislikeBeer(beer)
    }

    @Test
    fun onCreate_success(){
        subject.onCreate()
        Mockito.verify(view).displayBeer(beer)
        Mockito.verify(socialClient).getBeerRating(beer)
        Mockito.verify(socialClient).isBeerLiked(beer)
    }

    @Test
    fun getRating_success(){
        subject.getRating()
        Mockito.verify(socialClient).getBeerRating(beer)
    }

    @Test
    fun getLiked_success(){
        subject.getLiked()
        Mockito.verify(socialClient).isBeerLiked(beer)
    }

    @Test
    fun likeButtonClicked_success(){
        subject.likeButtonClicked(true)
        subject.likeButtonClicked(false)
        Mockito.verify(socialClient).likeBeer(beer)
        Mockito.verify(socialClient).dislikeBeer(beer)
    }
}