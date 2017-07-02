package com.nonvoid.barcrawler.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.datalayer.client.BreweryClient
import com.nonvoid.barcrawler.fragment.BreweryListFragment
import com.nonvoid.barcrawler.model.Brewery
import com.nonvoid.barcrawler.model.BreweryLocation
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_nav_drawer.*
import kotlinx.android.synthetic.main.search_view_layout.*
import java.util.ArrayList

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val client : BreweryClient = BreweryClient()
    val disposables : CompositeDisposable = CompositeDisposable();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpDrawerNav()
        setUpSearch()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("implement navigation item selected")
//        when (item.itemId){
//            R.id.nav_brewery ->
//        }
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
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

        city_search_button.setOnClickListener { v ->
            run {
                Log.d("MPG", "Trying to search")

                val dialog = showSearchDialog(v, "Loading breweries in ${search_edit_text.text}")

                val disposable = this.client.getLocationsInCity(search_edit_text.text.toString())
                    .subscribe(
                            {
                                list ->  setList(list)
                                dialog.dismiss()
                            },
                            {
                                throwable -> Log.e("", throwable.message, throwable)
                                dialog.dismiss()
                            }
                    )
                disposables.add(disposable)
            }
        }

        brewery_search_button.setOnClickListener { v ->
            run {
                val dialog = showSearchDialog(v, "Searching for breweries with ${search_edit_text.text} in the name")
                client.searchForBrewery(search_edit_text.text.toString())
                        .subscribe(
                                {
                                    list ->  setBreweryList(list)
                                    dialog.dismiss()},
                                {
                                    throwable -> Log.e("", throwable.message, throwable)
                                    dialog.dismiss()
                                }
                        )
            }
        }
    }

    private fun showSearchDialog(v : View, text :String): Dialog{
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        return ProgressDialog.show(this, "", text)
    }

    private lateinit var  breweryList: ArrayList<BreweryLocation>

    private fun setList(list: ArrayList<BreweryLocation>) {
        this.breweryList = list
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_layout, BreweryListFragment.newInstance(breweryList))
                .commit()
    }

    private fun setBreweryList (list: ArrayList<Brewery>){
        Snackbar.make(nav_view, " setBreweryList", Snackbar.LENGTH_LONG).show()
    }
}
