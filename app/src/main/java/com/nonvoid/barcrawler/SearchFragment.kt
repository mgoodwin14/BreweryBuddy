package com.nonvoid.barcrawler

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(){

    private lateinit var presenter: Searchable

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        disposables.add( RxView.clicks(search_fragment_button)
//                .map { search_fragment_edit_text.text.toString() }
//                .filter{ it.isBlank() && it.length > 3}
//                .map { presenter.doOnSearch(it) }
//                .subscribe()
//        )
    }

    companion object {
        fun newInstance(callback: Searchable): Fragment{
            val fragment = SearchFragment()
            fragment.presenter = callback
            return fragment
        }
    }

    interface Searchable {
        fun doOnSearch(query: String)
    }
}