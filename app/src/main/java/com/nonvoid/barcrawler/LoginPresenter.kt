package com.nonvoid.barcrawler

import android.support.design.widget.Snackbar
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

    private fun loginAnonymously(auth: FirebaseAuth): Observable<FirebaseUser>{
        return if(auth.currentUser!= null){
            Observable.just(auth .currentUser)
        }
        else{
            RxFirebaseAuth.signInAnonymously(auth)
                    .map { response -> response.user }
                    .toObservable()
        }
    }

    fun login(): Observable<FirebaseUser> {
        return Observable.just(FirebaseAuth.getInstance())
                .flatMap { loginAnonymously(it) }
    }

    interface View
}