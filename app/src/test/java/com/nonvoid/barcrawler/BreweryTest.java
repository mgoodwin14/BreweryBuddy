package com.nonvoid.barcrawler;

import android.support.annotation.NonNull;

import com.nonvoid.barcrawler.datalayer.api.BreweryAPI;
import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.datalayer.response.BeerResponse;
import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.BreweryLocation;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Matt on 5/12/2017.
 */

public class BreweryTest {

    BreweryAPI client = new BreweryClient();

    @BeforeClass
    public static void setUpRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    @Test
    public void testLocationService(){

        Assert.assertNotNull(client);

        TestObserver<ArrayList<BreweryLocation>> testSubscriber = client.getLocationsInCity("Columbus").test();

        testSubscriber.assertSubscribed();
        testSubscriber.assertComplete();
        testSubscriber.assertNoErrors();

        List list = testSubscriber.getEvents();
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testBeerService(){

        Assert.assertNotNull(client);

        TestObserver<ArrayList<Beer>> testSubscriber = client.getBeersForBrewery("DnuXce").test();

        testSubscriber.assertSubscribed();
        testSubscriber.assertComplete();
        testSubscriber.assertNoErrors();

        List list = testSubscriber.getEvents();

        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
    }
}
