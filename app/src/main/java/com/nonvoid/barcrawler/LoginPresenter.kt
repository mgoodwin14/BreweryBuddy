package com.nonvoid.barcrawler

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.Observable


/**
 * Created by Matt on 11/22/2017.
 */
class LoginPresenter {

    private var view:View? = null

    fun onViewCreated(view: View){
        this.view = view
    }

    fun onViewDestroyed(){
        view = null
    }

    fun login(): Observable<FirebaseUser> {
        Log.d("MPG","LoginPresenter.login()")

        return Observable.just(FirebaseAuth.getInstance())
                .flatMap { loginAnonymously(it) }
    }

    private fun loginAnonymously(auth: FirebaseAuth): Observable<FirebaseUser>{
        return if(auth.currentUser!= null){
            Observable.just(auth.currentUser)
        }
        else{
            RxFirebaseAuth.signInAnonymously(auth)
                    .map { response -> response.user }
                    .toObservable()
        }
    }

    interface View
}