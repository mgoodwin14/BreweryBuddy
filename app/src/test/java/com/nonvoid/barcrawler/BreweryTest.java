package com.nonvoid.barcrawler;

import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.datalayer.response.BeerResponse;
import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.BreweryLocation;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

/**
 * Created by Matt on 5/12/2017.
 */

public class BreweryTest {

    @Test
    public void testLocationService(){
        BreweryClient client = new BreweryClient();

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
        BreweryClient client = new BreweryClient();

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
