package com.nonvoid.barcrawler.brewery

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.dagger.MyApp
import com.nonvoid.barcrawler.database.BreweryDataBaseAPI
import com.nonvoid.barcrawler.model.BreweryLocation
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList
import javax.inject.Inject
import kotlinx.android.synthetic.main.brewery_list_fragment.*

/**
 * Created by Matt on 8/3/2017.
 */
class BreweryLocationFragment : Fragment(), BreweryLocationAdapter.Callback {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.brewery_list_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brewery_list_recyclerview.setHasFixedSize(true)
        brewery_list_recyclerview.layoutManager = LinearLayoutManager(context)


        if(arguments != null) {
            val list: List<BreweryLocation> = arguments.getParcelableArrayList(BREWERY_LOCATION_LIST_BUNDLE_KEY)
            displayList(list)
        }
    }

    fun displayList(list: List<BreweryLocation>){
        if(!list.isEmpty()){
            search_empty_state.visibility = View.GONE
            brewery_list_recyclerview.adapter = BreweryLocationAdapter(list, this)
            brewery_list_recyclerview.adapter.notifyDataSetChanged()
        }else {
            search_empty_state.visibility = View.VISIBLE
            brewery_list_recyclerview.visibility = View.GONE
        }
    }

    override fun onBrewerySelected(location: BreweryLocation?) {
        startActivity(BreweryDetailsActivity.newIntent(context, location))
    }

    companion object {

        private val BREWERY_LOCATION_LIST_BUNDLE_KEY = "brewery_locations_key"

        fun newInstance(locations: ArrayList<BreweryLocation>): BreweryLocationFragment {
            val fragment = BreweryLocationFragment()
            val args = Bundle()
            args.putParcelableArrayList(BREWERY_LOCATION_LIST_BUNDLE_KEY, locations)
            fragment.arguments = args
            return fragment
        }
    }
}