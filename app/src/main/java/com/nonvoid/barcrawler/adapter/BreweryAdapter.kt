package com.nonvoid.barcrawler.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nonvoid.barcrawler.R
import com.nonvoid.barcrawler.model.Brewery
import com.squareup.picasso.Picasso


/**
 * Created by Matt on 7/1/2017.
 */
class BreweryAdapter(private val list: ArrayList<Brewery>, private val callback: Callback) : RecyclerView.Adapter<BreweryAdapter.BreweryViewHolder>() {

    override fun onBindViewHolder(holder: BreweryViewHolder, position: Int) {
        holder.setView(list[position])
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
        val descriptionTextView = itemView.findViewById(R.id.brewery_list_brand_classification_textview) as TextView
        val locationTextView = itemView.findViewById(R.id.brewery_list_location_textview) as TextView
        val imageView = itemView.findViewById(R.id.brewery_image_view) as ImageView

        fun setView(brewery: Brewery){
            nameTextView.text = brewery.name
            descriptionTextView.text = brewery.brandClassification.toUpperCase()
            if(!brewery.locations.isEmpty()){
                //need to turn on premium features at
                //http://www.brewerydb.com/developers/premium
                //locationTextView.text = brewery.locations?.get(0)?.locality
            } else {
                locationTextView.visibility = View.GONE
            }

            Picasso.with(imageView.context)
                    .load(brewery.images?.large)
                    .into(imageView)
        }
    }

    interface Callback{
        fun onBrewerySelected(brewery: Brewery)
    }
}