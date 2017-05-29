package com.nonvoid.barcrawler;

import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.model.BreweryLocation;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;

import io.reactivex.observers.TestObserver;

/**
 * Created by Matt on 5/12/2017.
 */

public class BreweryTest {

    @Test
    public void testService(){
        BreweryClient client = new BreweryClient();

        Assert.assertNotNull(client);

//        TestObserver<List<BreweryLocation>> testSubscriber = client.getLocationsInCity("Columbus").test();
//
//        testSubscriber.assertSubscribed();
//        testSubscriber.assertComplete();
//        testSubscriber.assertNoErrors();
//
//        List list = testSubscriber.getEvents();
//        Assert.assertFalse(list.isEmpty());
    }
}
