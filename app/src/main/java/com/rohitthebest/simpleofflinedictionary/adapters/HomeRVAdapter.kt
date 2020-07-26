package com.rohitthebest.simpleofflinedictionary.adapters

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import kotlinx.android.synthetic.main.home_rv_layout.view.*

class HomeRVAdapter : ListAdapter<Word, HomeRVAdapter.DictionaryViewHolder>(DiffUtilCallback()) {

    var mListener: OnItemClickListener? = null

    inner class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        init {

            itemView.setOnClickListener(this)
            itemView.homeFragBookmarkButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when(v?.id){

                itemView.id  -> {
                    val pos = absoluteAdapterPosition
                    if (pos != RecyclerView.NO_POSITION && mListener != null) {

                        mListener?.onItemClick(getItem(pos))
                    }
                }

                itemView.homeFragBookmarkButton.id -> {

                    val pos = absoluteAdapterPosition
                    if (pos != RecyclerView.NO_POSITION && mListener != null) {

                        mListener?.onBookmarkBtnClicked(getItem(pos))
                    }
                }

            }
        }
    }

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

            if(getItem(position).isBookMarked == context.getString(R.string.t)){

                homeFragBookmarkButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_blue_24))
            }else{
                homeFragBookmarkButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_blue_border_24))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(word: Word)

        //For SavedFragment
        fun onBookmarkBtnClicked(word: Word?)
    }

    fun setOnClickListener(listener: OnItemClickListener) {

        mListener = listener
    }

}