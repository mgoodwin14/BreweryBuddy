package com.nonvoid.barcrawler.brewery

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.datalayer.io.BreweryDataBaseAPI
import com.nonvoid.barcrawler.SearchFragment.Searchable
import com.nonvoid.barcrawler.model.BreweryLocation
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList
import javax.inject.Inject
import kotlinx.android.synthetic.main.brewery_list_fragment.*

/**
 * Created by Matt on 8/3/2017.
 */
class BreweryLocationFragment : Fragment(), Searchable, BreweryLocationAdapter.Callback {

    @Inject
    lateinit var client: BreweryDataBaseAPI

    val breweryLocationList = ArrayList<BreweryLocation>()

    internal var loadingDialog: ProgressDialog? = null

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MyApp).netComponent.inject(this)

        if(arguments != null) {
            breweryLocationList.addAll(arguments.getParcelableArrayList(BREWERY_LOCATION_LIST_BUNDLE_KEY))
            brewery_list_recyclerview.adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.brewery_list_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brewery_list_recyclerview.setHasFixedSize(true)
        brewery_list_recyclerview.layoutManager = LinearLayoutManager(context)
        brewery_list_recyclerview.adapter = BreweryLocationAdapter(breweryLocationList, this)
        search_empty_state.text = "search by city"
    }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    override fun doOnSearch(query: String) {
         client.searchCityForBreweries(query)
                .doOnSubscribe{showLoading(true)}
                .doOnComplete({showLoading(false)})
                .doOnError({x -> Log.d("MPG", x.message, x)})
                .subscribe({list -> setList(list) })
    }

    fun setList(list: List<BreweryLocation>){
        if(list.isEmpty()){
            search_empty_state.visibility = View.VISIBLE
            brewery_list_recyclerview.visibility = View.GONE
        }else {
            search_empty_state.visibility = View.GONE
            breweryLocationList.clear()
            breweryLocationList.addAll(list)
            brewery_list_recyclerview.adapter.notifyDataSetChanged()
        }
    }

    override fun onBrewerySelected(location: BreweryLocation?) {
        startActivity(BreweryDetailsActivity.newIntent(context, location))
    }

    companion object {

        private val BREWERY_LOCATION_LIST_BUNDLE_KEY = "brewery_locations_key"

        fun newInstance(locations: List<BreweryLocation>): BreweryListFragment {
            val fragment = BreweryListFragment()
            val args = Bundle()
            args.putParcelableArrayList(BREWERY_LOCATION_LIST_BUNDLE_KEY, locations as ArrayList<out Parcelable>)
            fragment.arguments = args
            return fragment
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            loadingDialog = ProgressDialog.show(context, "", "Searching location for breweries", false, true)
        } else {
            loadingDialog?.dismiss()
        }
    }
}