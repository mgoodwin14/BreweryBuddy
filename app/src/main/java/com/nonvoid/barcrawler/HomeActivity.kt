package com.nonvoid.barcrawler

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.beer.BeerListFragment
import com.nonvoid.barcrawler.brewery.BreweryListFragment
import com.nonvoid.barcrawler.brewery.BreweryLocationFragment
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_nav_drawer.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val disposables : CompositeDisposable = CompositeDisposable()

    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        (application as MyApp).netComponent.inject(this)

        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser!=null){
            firebaseAuth.currentUser!!.reload()
            Log.d("MPG", "currentUser: ${firebaseAuth.currentUser}")
        }else {
            val dialog = ProgressDialog.show(this, "Logging in", "Please wait.", true)
            RxFirebaseAuth.signInAnonymously(firebaseAuth)
                    .doFinally{dialog.dismiss()}
                    .subscribe({result ->
                        if(result.user!= null){
                            Toast.makeText(this, "Auth success!", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(this, "Auth failed", Toast.LENGTH_LONG).show()
                        }
                    })
        }

        setUpDrawerNav()
        val breweryFragment = BreweryListFragment()
        val searchFragment = SearchFragment.newInstance(breweryFragment)
        supportFragmentManager.beginTransaction()
                .add(R.id.search_fragment_frame_layout, searchFragment, searchFragment.javaClass.simpleName)
                .add(R.id.content_frame_layout, breweryFragment, breweryFragment.javaClass.simpleName)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId){
            R.id.nav_brewery -> {
                supportFragmentManager.findFragmentByTag(BreweryListFragment::class.java.simpleName)?:
                        replaceContent(BreweryListFragment())
                return true
            }
            R.id.nav_location -> {
                supportFragmentManager.findFragmentByTag(BreweryLocationFragment::class.java.simpleName)?:
                        replaceContent(BreweryLocationFragment())
                return true
            }
            R.id.nav_beer -> {
                supportFragmentManager.findFragmentByTag(BeerListFragment::class.java.simpleName)?:
                        replaceContent(BeerListFragment())
                return true
            }
        }
        return false
    }

    fun replaceContent(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_layout, fragment)
                .commit()

        val searchFragment = supportFragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName) as SearchFragment
        searchFragment.setSearchable(fragment as SearchFragment.Searchable)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else if(supportFragmentManager.backStackEntryCount>1)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    private fun setUpDrawerNav() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }
}
