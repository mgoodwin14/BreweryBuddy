package com.nonvoid.barcrawler.redesign

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.widget.RxSearchView
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.beer.BeerListFragment
import com.nonvoid.barcrawler.brewery.BreweryListFragment
import com.nonvoid.barcrawler.brewery.BreweryLocationFragment
import com.nonvoid.barcrawler.dagger.MyApp
import kotlinx.android.synthetic.main.activity_home_redesign.*
import javax.inject.Inject

/**
 * Created by Matt on 11/23/2017.
 */

val breweryFragment by lazy { BreweryListFragment() }
val beerFragment by lazy { BeerListFragment() }
val locationFragment by lazy { BreweryLocationFragment() }

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_redesign)
        (application as MyApp).netComponent.inject(this)
        Log.d("MPG", "SearchActivity.onCreate()")

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(breweryFragment, "Brewery")
        viewPagerAdapter.addFragment(beerFragment, "Beer")
        viewPagerAdapter.addFragment(locationFragment, "Location")

        search_view_pager.adapter = viewPagerAdapter
        home_tab_layout.setupWithViewPager(search_view_pager)

        val textChange = RxSearchView.queryTextChanges(home_search_view)
                .map { it.toString() }
                .doOnNext { Log.d("MPG", "queryTextChanges: $it") }

        val brewerySearch = textChange
                .flatMap { presenter.searchBrewery(it) }
                .doOnNext { Log.d("MPG", "brewery search: $it") }
//                .doOnSubscribe { search_progress_bar.visibility = View.VISIBLE }
//                .doOnTerminate { search_progress_bar.visibility = View.GONE }
//                .doOnNext { search_progress_bar.visibility = View.GONE }

        breweryFragment.display(brewerySearch)

        val beerSearch = textChange
                .flatMap { presenter.searchBeer(it) }
                .doOnNext { Log.d("MPG", "beer search: $it") }

        val locationSearch = textChange
                .flatMap { presenter.searchLocation(it) }
                .doOnNext { Log.d("MPG", "location search: $it") }


        home_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d("MPG", "tabselected: ${home_tab_layout.selectedTabPosition} : ${tab.text}")

                when (home_tab_layout.selectedTabPosition) {
                    0 -> breweryFragment.display(brewerySearch)
                    1 -> beerFragment.display(beerSearch)
                    2 -> locationFragment.display(locationSearch)
                }
            }
        })
    }

    class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        private val fragmentList = mutableListOf<Fragment>()
        private val titleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment = fragmentList[position]
        override fun getCount(): Int = fragmentList.size
        override fun getPageTitle(position: Int): CharSequence = titleList[position]

        fun addFragment(fragment: Fragment, title: String) {
            Log.d("MPG", "addedFragmnet: $title")
            fragmentList.add(fragment)
            titleList.add(title)
        }
    }
}