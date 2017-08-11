package com.nonvoid.barcrawler

import com.nonvoid.barcrawler.datalayer.io.BreweryDataBaseService
import com.nonvoid.barcrawler.datalayer.io.BreweryDataBaseAPI
import com.nonvoid.barcrawler.datalayer.io.BreweryDataBaseClient
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import com.nonvoid.barcrawler.model.response.BeerResponse
import com.nonvoid.barcrawler.model.response.BreweryResponse
import com.nonvoid.barcrawler.model.response.LocationResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import org.junit.Test
import org.mockito.Mockito
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.BeforeClass
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Created by Matt on 8/10/2017.
 */
class BreweryDatabaseClientTest{


    val breweryDataBaseService: BreweryDataBaseService = Mockito.mock(BreweryDataBaseService::class.java)
    val subject : BreweryDataBaseAPI = BreweryDataBaseClient(breweryDataBaseService)

    val mockBreweryResponse = BreweryResponse()
    val mockBrewery = Brewery()
    val mockBeerResponse = BeerResponse()
    val mockBeer = Beer()
    val mockLocationResponse = LocationResponse()
    val mockLocation = BreweryLocation()

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpRxSchedulers() {
            val immediate = object : Scheduler() {
                override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                    // this prevents StackOverflowErrors when scheduling with a delay
                    return super.scheduleDirect(run, 0, unit)
                }

                override fun createWorker(): Worker {
                    return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
                }
            }

            RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
            RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
        }
    }

    @Before
    fun setUp(){
        mockBreweryResponse.breweries = listOf(mockBrewery)
        mockBeerResponse.beers = listOf(mockBeer)
        mockLocationResponse.locations = listOf(mockLocation)
    }

    @Test
    fun searchForBrewery_success(){
        Mockito.`when`(breweryDataBaseService.searchForBrewery(Mockito.anyString()))
                .thenReturn( Observable.just(mockBreweryResponse))

        subject.searchForBrewery("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBreweryResponse.breweries)
    }

    @Test
    fun getBreweryById_success(){
        Mockito.`when`(breweryDataBaseService.getBreweryById(Mockito.anyString()))
                .thenReturn(Observable.just(mockBreweryResponse))

        subject.getBreweryById("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBreweryResponse.breweries[0])
    }

    @Test
    fun getBreweryById_notFound(){
        mockBreweryResponse.breweries = listOf()
        Mockito.`when`(breweryDataBaseService.getBreweryById(Mockito.anyString()))
                .thenReturn(Observable.just(mockBreweryResponse))

        subject.getBreweryById("").test()
                .assertNotComplete()
    }

    @Test
    fun searchForBreweriesInCity_success(){

        Mockito.`when`(breweryDataBaseService.searchCityForBreweries(Mockito.anyString()))
                .thenReturn(Observable.just(mockLocationResponse))

        subject.searchCityForBreweries("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockLocationResponse.locations)
    }

    @Test
    fun getBeerById_success(){
        Mockito.`when`(breweryDataBaseService.getBeerById(Mockito.anyString()))
                .thenReturn(Observable.just(mockBeerResponse))

        subject.getBeerById("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBeerResponse.beers[0])
    }

    @Test
    fun getBeerById_notFound(){
        mockBeerResponse.beers = listOf()
        Mockito.`when`(breweryDataBaseService.getBeerById(Mockito.anyString()))
                .thenReturn(Observable.just(mockBeerResponse))

        subject.getBeerById("").test()
                .assertNotComplete()
    }

    @Test
    fun searchForBeer_success(){

        Mockito.`when`(breweryDataBaseService.searchForBeer(Mockito.anyString()))
                .thenReturn(Observable.just(mockBeerResponse))

        subject.searchForBeer("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBeerResponse.beers)
    }

    @Test
    fun getBeersForBrewery_success(){
        Mockito.`when`(breweryDataBaseService.getBeersForBrewery(Mockito.anyString()))
                .thenReturn(Observable.just(mockBeerResponse))

        subject.getBeersForBrewery("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBeerResponse.beers)
    }
}