package com.nonvoid.barcrawler.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nonvoid.barcrawler.R
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(){

    private lateinit var callback : Searchable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_fragment_button.setOnClickListener({
            if(!search_fragment_edit_text.text.toString().isNullOrBlank())
                callback.doOnSearch(search_fragment_edit_text.text.toString())
        })
    }

    fun setSearchable(searchable: Searchable){
        callback = searchable
        search_fragment_edit_text.setText("")
    }

    companion object {
        fun newInstance(callback: Searchable): Fragment{
            val fragment = SearchFragment()
            fragment.callback = callback
            return fragment
        }
    }

    interface Searchable {
        fun doOnSearch(query: String)
    }
}