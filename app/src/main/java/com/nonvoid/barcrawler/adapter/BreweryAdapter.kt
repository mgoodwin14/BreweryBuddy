package com.nonvoid.barcrawler.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.model.Brewery


/**
 * Created by Matt on 7/1/2017.
 */
class BreweryAdapter(private val list: ArrayList<Brewery>, private val callback: Callback) : RecyclerView.Adapter<BreweryAdapter.BreweryViewHolder>() {

    override fun onBindViewHolder(holder: BreweryViewHolder, position: Int) {
        holder.nameTextView.text = list[position].name
        holder.descriptionTextView.text = list[position].brandClassification
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreweryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brewery_list_row, parent, false)
        view.setOnClickListener { v ->
            val position = (v.parent as RecyclerView).getChildLayoutPosition(v)
            callback.onBrewerySelected(list[position])
        }
        return BreweryViewHolder(view)
    }

    class BreweryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTextView = itemView.findViewById(R.id.brewery_list_name_textview) as TextView
        val descriptionTextView = itemView.findViewById(R.id.brewery_list_description_textview) as TextView
    }

    interface Callback{
        fun onBrewerySelected(brewery: Brewery)
    }
}