package com.nonvoid.barcrawler

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.social.FireBaseSocialClient
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito

/**
 * Created by Matt on 8/19/2017.
 */
class FirebaseSocialClientTest{

    val brewery = Brewery()
    val user = Mockito.mock(FirebaseUser::class.java)
    val reference = Mockito.mock(DatabaseReference::class.java)

    val subject = FireBaseSocialClient(user, reference)

    @Before
    fun setUp(){
        Mockito.`when`(reference.child(Mockito.anyString()))
                .thenReturn(reference)
    }

    @Ignore
    @Test
    fun favoriteBrewery_success(){
        //can't test because of Static RxFirebaseDatabase method setValue
        subject.favoriteBrewery(brewery)
    }
}