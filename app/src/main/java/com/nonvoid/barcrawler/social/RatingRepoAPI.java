package com.nonvoid.barcrawler.social;

import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.Brewery;

import io.reactivex.Maybe;

/**
 * Created by Matt on 8/10/2017.
 */

public interface RatingRepoAPI {
    void setBreweryAsFavorite(Brewery brewery, Boolean favorite);
    Maybe<Integer> getNumberOfFavoritesForBrewery(Brewery brewery);
    Maybe<Boolean> isBeerLiked(Beer beer);
    Maybe<Double> getBeerRating(Beer beer);
    void likeBeer(Beer beer);
    void dislikeBeer(Beer beer);
}
