package com.nonvoid.barcrawler

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.nonvoid.barcrawler.dagger.MyApp
import kotlinx.android.synthetic.main.activity_home.*


/**
 * Created by Matt on 11/22/2017.
 */
class LoginActivity : AppCompatActivity(), LoginPresenter.View{

    private val presenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        (application as MyApp).netComponent.inject(this)

        presenter.onViewCreated(this)
    }

    override fun onResume() {
        super.onResume()
         presenter.login()
                 .doOnSubscribe{ showLoading()}
                 .doOnDispose{ hideLoading()}
                 .doOnError { Log.e("MPG", "error logging in", it) }
                 .doOnNext { user -> user.reload() }
                 .subscribe{ goToHome()}
    }

    private fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun goToHome() {
        Snackbar.make(nav_view, "Welcome back", Snackbar.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}