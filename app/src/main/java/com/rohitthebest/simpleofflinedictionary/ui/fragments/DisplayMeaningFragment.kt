package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.adapters.DisplayMeaningAdapter
import com.rohitthebest.simpleofflinedictionary.model.Definition
import com.rohitthebest.simpleofflinedictionary.model.Word
import kotlinx.android.synthetic.main.fragment_display_meaning.*

class DisplayMeaningFragment : Fragment(R.layout.fragment_display_meaning) {

    private var receivedWord: Word? = null
    private var mAdapter: DisplayMeaningAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = DisplayMeaningAdapter()
        getMessage()

        closeDisplayMeaningFrag.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getMessage() {

        if (!arguments?.isEmpty!!) {

            val args = arguments?.let {
                DisplayMeaningFragmentArgs.fromBundle(it)
            }

            val message = args?.wordMessage
            val gson = Gson()
            val type = object : TypeToken<Word>() {}.type

            receivedWord = gson.fromJson(message, type)

            updateUI(receivedWord)
            setUpRecyclerView(receivedWord?.definitions)

        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(receivedWord: Word?) {

        receivedWord?.let {

            if (it.pronunciation == "null" || it.pronunciation == null) {
                displayMeaningFragTitleTV.text =
                    "${receivedWord.word}"
            } else {
                displayMeaningFragTitleTV.text =
                    "${receivedWord.word} (${receivedWord.pronunciation})"
            }
        }

    }

    private fun setUpRecyclerView(definitions: List<Definition>?) {

        try {

            definitions?.let {

                mAdapter?.submitList(it)

                displayMeaningFragRV.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}