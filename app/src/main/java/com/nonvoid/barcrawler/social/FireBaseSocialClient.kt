package com.nonvoid.barcrawler.social

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.*

/**
 * Created by Matt on 8/8/2017.
 */
class FireBaseSocialClient(private val user: FirebaseUser,
                           private val reference :DatabaseReference = FirebaseDatabase.getInstance().reference)
    : SocialRepoAPI {

    override fun favoriteBrewery(brewery: Brewery) {
        setBreweryAsFavorite(brewery, true)
    }

    override fun unfavoriteBrewery(brewery: Brewery) {
        setBreweryAsFavorite(brewery,false)
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

    override fun isBeerLiked(beer: Beer): Maybe<Boolean> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                getBeerRatingReference(beer).child(user.uid).orderByValue())
                .filter({data->data.exists()})
                .map {snapShot -> (snapShot.value as Long).toInt() == 1 }
    }


    override fun getBeerRating(beer: Beer): Single<Double> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                getBeerRatingReference(beer).orderByValue())
                .map({snapshot ->
                    run{
                        var rating = 0.0
                        if(snapshot.hasChildren()){
                            rating =(snapshot.value as Map<String, Double>).values.sum() / snapshot.childrenCount.toDouble()
                        }
                        (rating)
                    }
                }).toSingle()
    }

    override fun getReviews(beer: Beer): Maybe<List<String>> {
        return RxFirebaseDatabase.observeSingleValueEvent(
                getBeerReviewReference(beer))
                .map ({ dataSnapshot -> run{
                    if(dataSnapshot.hasChildren()){
                        val reviews = (dataSnapshot.value as Map<String, String>).values.toList()
                        reviews
                    }else{
                        Collections.emptyList<String>()
                    }
                }
                })
    }

    override fun likeBeer(beer: Beer){
        rateBeer(beer, 1)
    }

    override fun dislikeBeer(beer: Beer){
        rateBeer(beer, 0)
    }

    private fun setBreweryAsFavorite(brewery: Brewery, favorite: Boolean) {
        RxFirebaseDatabase.setValue(getBreweryFavoriteReference(brewery)
                .child(user.uid), favorite)
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG","setBreweryAsFavorite: $favorite") }
                .subscribe()

        RxFirebaseDatabase.setValue(reference.child(USER).child(user.uid).child(FAVORITE).child(brewery.id), favorite)
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG", "added brewery to user favorrites") }
                .subscribe()
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

        RxFirebaseDatabase.setValue(reference.child(USER).child(user.uid).child(RATING).child(beer.id), rating)
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG", "added beer to user ratings") }
                .subscribe()
    }

    override fun submitReview(beer: Beer, message: String) {
        RxFirebaseDatabase.setValue(getBeerReviewReference(beer).child(user.uid), message)
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG","submitted review: $message") }
                .subscribe()
    }

    override fun submitComment(brewery: Brewery, message: String) {
        val ref = getBreweryCommentReference(brewery).push()
        RxFirebaseDatabase.setValue(ref, Pair(user.uid, message))
                .doOnError({throwable -> Log.d("MPG",throwable.message, throwable)} )
                .doOnComplete { Log.d("MPG","submitted comment: $message") }
                .subscribe()
    }

    private fun getBreweryFavoriteReference(brewery: Brewery): DatabaseReference{
        return reference.child(BREWERY)
                .child(brewery.id)
                .child(FAVORITE)
    }

    private fun getBreweryCommentReference(brewery: Brewery): DatabaseReference{
        return reference.child(BREWERY)
                .child(brewery.id)
                .child(COMMENT)
    }

    private fun getBeerRatingReference(beer: Beer): DatabaseReference{
        return reference.child(BEER)
                .child(beer.id)
                .child(RATING)
    }

    private fun getBeerReviewReference(beer: Beer): DatabaseReference {
        return reference.child(BEER)
                .child(beer.id)
                .child(REVIEW)
    }

    companion object {
        const val USER = "user"
        const val BREWERY = "brewery"
        const val FAVORITE = "favorite"
        const val COMMENT = "comment"
        const val BEER = "beer"
        const val RATING = "rating"
        const val REVIEW = "review"
    }
}