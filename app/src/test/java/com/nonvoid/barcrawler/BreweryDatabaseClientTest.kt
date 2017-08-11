package com.nonvoid.barcrawler

import com.nonvoid.barcrawler.datalayer.io.BeerService
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
import io.reactivex.subscribers.TestSubscriber
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import retrofit2.Response
import java.util.*
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.Scheduler.Worker
import io.reactivex.disposables.Disposable
import org.junit.BeforeClass
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Created by Matt on 8/10/2017.
 */
class BreweryDatabaseClientTest{


    val beerService :BeerService = Mockito.mock(BeerService::class.java)
    val subject : BreweryDataBaseAPI = BreweryDataBaseClient(beerService)

    val mockBreweryResponse = BreweryResponse()
    val mockBrewery = Brewery()
    val mockBeerResponse = BeerResponse()
    val mockBeer = Beer()

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

    @Test
    fun searchForBrewery(){
        mockBreweryResponse.breweries = listOf(mockBrewery)

        Mockito.`when`(beerService.searchForBrewery(Mockito.anyString()))
                .thenReturn( Observable.just(mockBreweryResponse))

        subject.searchForBrewery("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBreweryResponse.breweries)
    }

    @Test
    fun searchForBeer(){
        mockBeerResponse.beers = listOf(mockBeer)

        Mockito.`when`(beerService.searchForBeer(Mockito.anyString()))
                .thenReturn(Observable.just(mockBeerResponse))

        subject.searchForBeer("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockBeerResponse.beers)
    }

    @Test
    fun searchForBreweriesInCity(){
        val mockLocationResponse = LocationResponse()
        val mockLocation = BreweryLocation()

        mockLocationResponse.locations = listOf(mockLocation)

        Mockito.`when`(beerService.searchCityForBreweries(Mockito.anyString()))
                .thenReturn(Observable.just(mockLocationResponse))

        subject.searchCityForBreweries("").test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(mockLocationResponse.locations)
    }


}