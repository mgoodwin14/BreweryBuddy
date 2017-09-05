package com.nonvoid.barcrawler.social

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.beer.BeerListFragment
import com.nonvoid.barcrawler.brewery.BreweryListFragment
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.Beer
import com.nonvoid.barcrawler.model.Brewery
import kotlinx.android.synthetic.main.profile_fragment.*
import javax.inject.Inject

class ProfileFragment: Fragment(), ProfilePresenter.ProfileView {

    @Inject
    lateinit var client: BreweryDataBaseAPI

    lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as MyApp).netComponent.inject(this)
        presenter = ProfilePresenter(this, client)
        presenter.onCreate()
    }

    override fun displayFavoriteBreweries(breweryList: List<Brewery>) {
        if(!breweryList.isEmpty()) {
            no_breweries_text_view.visibility = View.GONE
            val fragment = BreweryListFragment.newInstance( ArrayList(breweryList) )
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.favorite_breweries_frame_layout, fragment)
                    .commit()
        }
    }

    override fun displayLikedBeers(beerList: List<Beer>) {
        if(!beerList.isEmpty()){
            val fragment = BeerListFragment.newInstance(ArrayList(beerList))
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.favorite_breweries_frame_layout, fragment)
                    .commit()
        }
    }
}