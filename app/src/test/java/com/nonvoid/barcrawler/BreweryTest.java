package com.nonvoid.barcrawler;

import com.nonvoid.barcrawler.datalayer.client.BreweryClient;
import com.nonvoid.barcrawler.model.BreweryLocation;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;

import rx.observers.TestSubscriber;

/**
 * Created by Matt on 5/12/2017.
 */

public class BreweryTest {

    @Test
    public void testService(){
        BreweryClient client = new BreweryClient();

        Assert.assertNotNull(client);

        TestSubscriber testSubscriber = new TestSubscriber<List<BreweryLocation>>();

        client.getLocationsInCity("Columbus").subscribe(testSubscriber);

        testSubscriber.assertCompleted();
        testSubscriber.assertNoErrors();

        List list = testSubscriber.getOnNextEvents();
        Assert.assertFalse(list.isEmpty());
    }
}
