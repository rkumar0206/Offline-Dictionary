package com.rohitthebest.simpleofflinedictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.model.Word
import kotlinx.android.synthetic.main.home_rv_layout.view.*

class HomeRVAdapter : ListAdapter<Word, HomeRVAdapter.DictionaryViewHolder>(DiffUtilCallback()) {

    inner class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    class DiffUtilCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {

            return oldItem.word == newItem.word &&
                    oldItem.definitions?.get(0)?.definition == newItem.definitions?.get(0)?.definition &&
                    oldItem.definitions?.get(0)?.type == newItem.definitions?.get(0)?.type &&
                    oldItem.definitions?.get(0)?.example == newItem.definitions?.get(0)?.example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder {

        return DictionaryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.home_rv_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {

        holder.itemView.apply {

            homeFragWordTV.text = getItem(position).word
        }
    }
}