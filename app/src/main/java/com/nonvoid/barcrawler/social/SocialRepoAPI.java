package com.nonvoid.barcrawler.social;

import com.nonvoid.barcrawler.model.Beer;
import com.nonvoid.barcrawler.model.Brewery;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Matt on 8/10/2017.
 */

public interface SocialRepoAPI {
    Single<Boolean> isBreweryFavorited(Brewery brewery);
    Single<Integer> getNumberOfFavoritesForBrewery(Brewery brewery);
    Maybe<Boolean> isBeerLiked(Beer beer);
    Single<Integer> getBeerRating(Beer beer);
    Maybe<List<String>> getReviews(Beer beer);
    void favoriteBrewery(Brewery brewery);
    void unfavoriteBrewery(Brewery brewery);
    void likeBeer(Beer beer);
    void dislikeBeer(Beer beer);
    void submitReview(Beer beer, String message);
}
