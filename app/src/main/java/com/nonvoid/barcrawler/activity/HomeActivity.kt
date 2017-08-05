package com.nonvoid.barcrawler.activity

import android.app.Dialog

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
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.datalayer.api.BreweryAPI
import com.nonvoid.barcrawler.fragment.BeerListFragment
import com.nonvoid.barcrawler.fragment.BreweryListFragment
import com.nonvoid.barcrawler.fragment.BreweryLocationFragment
import com.nonvoid.barcrawler.fragment.SearchFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_nav_drawer.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchFragment.Searchable {

    @Inject
    lateinit var client : BreweryAPI

    val disposables : CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        (application as MyApp).netComponent.inject(this)

        setUpDrawerNav()
//        setUpSearch()
        val breweryFragment = BreweryListFragment()
        val searchFragment = SearchFragment.newInstance(breweryFragment)
        supportFragmentManager.beginTransaction()
                .add(R.id.search_fragment_frame_layout, searchFragment, searchFragment.javaClass.simpleName)
                .add(R.id.content_frame_layout, breweryFragment, breweryFragment.javaClass.simpleName)
                .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        TODO("implement navigation item selected")
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

    private fun setUpSearch(){

        val searchFragment = SearchFragment.newInstance(this)
        supportFragmentManager.beginTransaction()
                .add(R.id.search_fragment_frame_layout, searchFragment)
                .commit()

//        city_search_button.setOnClickListener { v ->
//            run {
//                Log.d("MPG", "Trying to search")
//
//                val dialog = showSearchDialog(v, "Loading breweries in ${search_edit_text.text}")
//
//                val disposable = this.client.getLocationsInCity(search_edit_text.text.toString())
//                    .subscribe(
//                            {
//                                list ->  supportFragmentManager.beginTransaction()
//                                    .replace(R.id.content_frame_layout, BreweryListFragment.newInstance(list))
//                                    .commit()
//                                dialog.dismiss()
//                            },
//                            {
//                                throwable -> Log.e("", throwable.message, throwable)
//                                dialog.dismiss()
//                            }
//                    )
//                disposables.add(disposable)
//            }
//        }
//
//        brewery_search_button.setOnClickListener { v ->
//            run {
//                val dialog = showSearchDialog(v, "Searching for breweries with ${search_edit_text.text} in the name")
//                client.searchForBrewery(search_edit_text.text.toString())
//                        .subscribe(
//                                {
//                                    list ->  supportFragmentManager.beginTransaction()
//                                        .replace(R.id.content_frame_layout, BreweryListFragment.newInstance(list))
//                                        .commit()
//                                    dialog.dismiss()},
//                                {
//                                    throwable -> Log.e("", throwable.message, throwable)
//                                    dialog.dismiss()
//                                }
//                        )
//            }
//        }
//
//        beer_search_button.setOnClickListener { v ->
//            run{
//                val dialog = showSearchDialog(v, "Searching for beers named ${search_edit_text.text}")
//                client.searchForBeer(search_edit_text.text.toString())
//                        .subscribe(
//                                {
//                                    list -> supportFragmentManager.beginTransaction()
//                                        .replace(R.id.content_frame_layout, BeerListFragment.newInstance(list))
//                                        .commit()
//                                    dialog.dismiss()
//                                },
//                                {
//                                    throwable -> Log.e("", throwable.message, throwable)
//                                    dialog.dismiss()
//                                }
//                        )
//            }
//        }
    }

    override fun doOnSearch(query: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun showSearchDialog(v : View, text :String): Dialog{
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        return ProgressDialog.show(this, "", text)
    }
}
