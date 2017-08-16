package com.nonvoid.barcrawler.social

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nonvoid.barcrawler.R

/**
 * Created by Matt on 8/15/2017.
 */
class BeerReviewAdapter(val list: List<String>) : RecyclerView.Adapter<BeerReviewAdapter.ReviewsViewHolder>() {
    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) = holder.setView(list[position])


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.beer_review_row, parent, false)
        return ReviewsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    class ReviewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val messageTextView :TextView = itemView.findViewById(R.id.beer_review_message) as TextView

        fun setView(message: String){
            messageTextView.text = message
        }
    }
}