package com.nonvoid.barcrawler.datalayer.client

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nonvoid.barcrawler.model.Brewery
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Observer
import java.util.*

/**
 * Created by Matt on 8/8/2017.
 */
class FireBaseClient(private val user: FirebaseUser) {
    private val reference :DatabaseReference = FirebaseDatabase.getInstance().reference

    fun setBreweryAsFavorite(brewery: Brewery, favorite: Boolean) {
        getBreweryReference()
            .child(brewery.id)
            .child(FAVORITE)
            .child(user.uid)
            .setValue(favorite)
            .addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Log.d("MPG", "set favorite to $favorite")
                }else {
                    Log.d("MPG", "failed to set favorite to $favorite")
                }
            }
    }

    fun getNumberOfFavoritesForBrewery(brewery: Brewery): Maybe<Int> {

        return RxFirebaseDatabase.observeSingleValueEvent(getBreweryReference()
                .child(brewery.id)
                .child(FAVORITE)
                .orderByValue()
                .equalTo(true), {snapShot: DataSnapshot -> snapShot.childrenCount.toInt()  }
        )
    }

    private fun getBreweryReference(): DatabaseReference{
        return reference.child(BREWERY)
    }

    companion object {
        const val BREWERY = "brewery"
        const val FAVORITE = "favorite"
    }
}