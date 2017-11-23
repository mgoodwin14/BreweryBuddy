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
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import com.nonvoid.barcrawler.social.ProfileFragment
import kotlinx.android.synthetic.main.beer_details_activity.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        HomePresenter.HomeView, SearchFragment.Searchable {

    @Inject
    lateinit var presenter: HomePresenter

    private var currentContent: String = BreweryListFragment::class.java.simpleName

    private var dialog: ProgressDialog? = null
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        (application as MyApp).netComponent.inject(this)

        setUpDrawerNav()

//        presenter = HomePresenter( client)
        presenter.onViewCreated(this)
        addSearchFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        drawer_layout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_brewery -> {
                supportFragmentManager.findFragmentByTag(BreweryListFragment::class.java.simpleName) ?:
                        displayContent(BreweryListFragment())
                return true
            }
            R.id.nav_location -> {
                supportFragmentManager.findFragmentByTag(BreweryLocationFragment::class.java.simpleName) ?:
                        displayContent(BreweryLocationFragment())
                return true
            }
            R.id.nav_beer -> {
                supportFragmentManager.findFragmentByTag(BeerListFragment::class.java.simpleName) ?:
                        displayContent(BeerListFragment())
                return true
            }
            R.id.nav_profile -> {
                displayProfileFragment()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

    override fun doOnSearch(query: String) {

        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

        when (currentContent) {
            BreweryListFragment::class.java.simpleName -> presenter.brewerySearch(query)
                    .doOnSubscribe { showDialog("Searching for breweries", "please wait") }
                    .doOnDispose { dismissDialog() }
                    .subscribe(this::displayBreweryList)

            BeerListFragment::class.java.simpleName -> presenter.beerSearch(query)
                    .doOnSubscribe { showDialog("Searching for beers", "please wait") }
                    .doFinally { dismissDialog() }
                    .subscribe(this::displayBeerFragment)

            BreweryLocationFragment::class.java.simpleName -> presenter.locationSearch(query)
                    .doOnSubscribe { showDialog("Searching by location", "please wait") }
                    .doOnDispose { dismissDialog() }
                    .subscribe(this::displayLocationFragment)
        }
    }

    private fun addSearchFragment() {
        val searchFragment = SearchFragment.newInstance(this)
        val breweryFragment = BreweryListFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.search_fragment_frame_layout, searchFragment, searchFragment.javaClass.simpleName)
                .add(R.id.content_frame_layout, breweryFragment, breweryFragment.javaClass.simpleName)
                .commit()
    }

    private fun displayBreweryList(breweryList: List<Brewery>) {
        val breweryFragment = BreweryListFragment.newInstance(ArrayList(breweryList))
        displayContent(breweryFragment)
    }

    private fun displayLocationFragment(locationList: List<BreweryLocation>) {
        val locationFragment = BreweryLocationFragment.newInstance(ArrayList(locationList))
        displayContent(locationFragment)
    }

    private fun displayBeerFragment(beerList: List<Beer>) {
        val beerFragment = BeerListFragment.newInstance(ArrayList(beerList))
        displayContent(beerFragment)
    }

    private fun displayProfileFragment() {
        val searchFrag = supportFragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
        supportFragmentManager.beginTransaction().remove(searchFrag).commit()
        displayContent(profileFragment)
    }

    private fun showDialog(title: String, message: String) {
        dialog = ProgressDialog.show(this, title, message, true)
    }

    private fun dismissDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    private fun displayContent(fragment: Fragment) {
        currentContent = fragment::class.java.simpleName

        if (profileFragment.isVisible) {
            addSearchFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_layout, fragment, fragment::class.java.simpleName)
                .commit()
    }

    private fun setUpDrawerNav() {
        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View?) {
                super.onDrawerOpened(drawerView)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(review_message_edit_text.windowToken, 0)
            }
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }
}
