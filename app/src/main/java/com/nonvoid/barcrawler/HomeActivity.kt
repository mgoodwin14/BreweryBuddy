package com.nonvoid.barcrawler

import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.beer.BeerListFragment
import com.nonvoid.barcrawler.brewery.BreweryListFragment
import com.nonvoid.barcrawler.brewery.BreweryLocationFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_nav_drawer.*
import android.net.ConnectivityManager
import android.view.inputmethod.InputMethodManager
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomePresenter.HomeView, SearchFragment.Searchable {

    @Inject
    lateinit var client: BreweryDataBaseAPI

    lateinit var presenter : HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        (application as MyApp).netComponent.inject(this)

        presenter = HomePresenter(this, client)
        presenter.onCreate()

        setUpDrawerNav()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
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

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else if(supportFragmentManager.backStackEntryCount>1)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

    override fun addSearchFragment() {
        val searchFragment = SearchFragment()
        searchFragment.setSearchable(this)
        val breweryFragment = BreweryListFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.search_fragment_frame_layout, searchFragment, searchFragment.javaClass.simpleName)
                .add(R.id.content_frame_layout, breweryFragment, breweryFragment.javaClass.simpleName)
                .commit()
    }

    override fun doOnSearch(query: String) {

        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

        supportFragmentManager.findFragmentByTag(BreweryListFragment::class.java.simpleName)?.let {
            presenter.brewerySearch(query)
            return
        }
        supportFragmentManager.findFragmentByTag(BeerListFragment::class.java.simpleName)?.let {
            presenter.beerSearch(query)
            return
        }
        supportFragmentManager.findFragmentByTag(BreweryLocationFragment::class.java.simpleName)?.let {
            presenter.locationSearch(query)
            return
        }
    }

    override fun displayBreweryList(breweryList: List<Brewery>) {
        val breweryFragment = BreweryListFragment.newInstance(ArrayList(breweryList))
        replaceContent(breweryFragment)
    }

    override fun displayLocationFragment(locationList: List<BreweryLocation>) {
        val locationFragment = BreweryLocationFragment.newInstance(ArrayList(locationList))
        replaceContent(locationFragment)
    }

    override fun displayBeerFragment(beerList: List<Beer>) {
        val beerFragment = BeerListFragment.newInstance(ArrayList(beerList))
        replaceContent(beerFragment)
    }

    override fun showDialog(title: String, message: String): ProgressDialog {
        return ProgressDialog.show(this, title, message, true)
    }

    override fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun replaceContent(fragment: Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_layout, fragment, fragment::class.java.simpleName)
                .commit()
    }

    private fun isNetworkVailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun setUpDrawerNav() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }
}
