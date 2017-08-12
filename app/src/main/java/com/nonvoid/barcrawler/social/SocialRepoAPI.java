package com.nonvoid.barcrawler.social;

import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.Brewery;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Matt on 8/10/2017.
 */

public interface SocialRepoAPI {
    Single<Boolean> isBreweryFavorited(Brewery brewery);
    Single<Integer> getNumberOfFavoritesForBrewery(Brewery brewery);
    Single<Boolean> isBeerLiked(Beer beer);
    Single<Integer> getBeerRating(Beer beer);
    void setBreweryAsFavorite(Brewery brewery, Boolean favorite);
    void likeBeer(Beer beer);
    void dislikeBeer(Beer beer);
}
