package com.nonvoid.barcrawler.social

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Matt on 8/8/2017.
 */
class FireBaseSocialClient(private val user: FirebaseUser) : SocialRepoAPI {

    private val reference :DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun setBreweryAsFavorite(brewery: Brewery, favorite: Boolean) {
        RxFirebaseDatabase.setValue(getBreweryFavoriteReference(brewery)
                .child(user.uid), favorite)
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG","setBreweryAsFavorite: $favorite") }
                .subscribe()
    }

    override fun isBreweryFavorited(brewery: Brewery): Single<Boolean> {
        return RxFirebaseDatabase.observeSingleValueEvent(getBreweryFavoriteReference(brewery)
                .child(user.uid))
                .map { snapShot -> run{
                    if(snapShot.exists()) {
                        snapShot.value as Boolean
                    }else {
                        false
                    }
                } }
                .toSingle()
    }

    override fun getNumberOfFavoritesForBrewery(brewery: Brewery): Single<Int> {
        return RxFirebaseDatabase.observeSingleValueEvent(getBreweryFavoriteReference(brewery)
                .orderByValue()
                .equalTo(true), {snapShot -> snapShot.childrenCount.toInt()}
        ).toSingle()
    }

    override fun isBeerLiked(beer: Beer): Single<Boolean> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                getBeerRatingReference(beer).child(user.uid).orderByValue())
                .filter({data->data.exists()})
                .map {snapShot -> (snapShot.value as Long).toInt() == 1 }
                .toSingle()
    }


    override fun getBeerRating(beer: Beer): Single<Int> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                getBeerRatingReference(beer).orderByValue())
                .map({snapshot ->
                    run{
                        var rating :Double = 0.0
                        if(snapshot.hasChildren()){
                            rating =(snapshot.value as Map<String, Double>).values.sum() / snapshot.childrenCount.toDouble()
                        }
                        (rating*100).toInt()
                    }
                }).toSingle()
    }

    override fun likeBeer(beer: Beer){
        rateBeer(beer, 1)
    }

    override fun dislikeBeer(beer: Beer){
        rateBeer(beer, 0)
    }

    private fun rateBeer(beer: Beer, rating: Int){
        getBeerRatingReference(beer)
                .child(user.uid)
                .setValue(rating)
                .addOnCompleteListener { result ->
                    if(result.isSuccessful){
                        Log.d("MPG", "set rating to $rating")
                    }else {
                        Log.d("MPG", "failed to set rating to $rating")
                    }
                }

    }

    private fun getBreweryFavoriteReference(brewery: Brewery): DatabaseReference{
        return reference.child(BREWERY)
                .child(brewery.id)
                .child(FAVORITE)
    }

    private fun getBeerRatingReference(beer: Beer): DatabaseReference{
        return reference.child(BEER)
                .child(beer.id)
                .child(RATING)

    }

    companion object {
        const val BREWERY = "brewery"
        const val FAVORITE = "favorite"
        const val BEER = "beer"
        const val RATING = "rating"
    }
}