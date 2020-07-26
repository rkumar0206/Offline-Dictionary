package com.rohitthebest.simpleofflinedictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.database.model.Definition
import com.rohitthebest.simpleofflinedictionary.others.Functions.Companion.isInternetAvailable
import kotlinx.android.synthetic.main.display_meaning_rv_layout.view.*

class DisplayMeaningAdapter :
    ListAdapter<Definition, DisplayMeaningAdapter.DisplayMeaningViewHolder>(DiffUtilCallback()) {

    inner class DisplayMeaningViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    class DiffUtilCallback : DiffUtil.ItemCallback<Definition>() {
        override fun areItemsTheSame(oldItem: Definition, newItem: Definition): Boolean {

            return oldItem.definition == newItem.definition
        }

        override fun areContentsTheSame(oldItem: Definition, newItem: Definition): Boolean {

            return oldItem.definition == newItem.definition &&
                    oldItem.type == newItem.type &&
                    oldItem.example == newItem.example

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayMeaningViewHolder {

        return DisplayMeaningViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.display_meaning_rv_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DisplayMeaningViewHolder, position: Int) {

        holder.itemView.apply {

            getItem(position)?.let {


                if (it.image_url == null || !isInternetAvailable(context)) {
                    displayMeaningFragImage.visibility = View.GONE
                } else {
                    displayMeaningFragImage.visibility = View.VISIBLE

                    Glide.with(this)
                        .load(it.image_url)
                        .into(displayMeaningFragImage)
                }

                displayMeaningFragDefinitionTV.text = it.definition
                displayMeaningFragTypeTV.text = it.type
                displayMeaningFragExampleTV.text = it.example
            }


        }
    }


}