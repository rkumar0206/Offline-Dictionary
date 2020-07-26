package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.adapters.DisplayMeaningAdapter
import com.rohitthebest.simpleofflinedictionary.database.model.Definition
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import com.rohitthebest.simpleofflinedictionary.ui.viewModels.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_display_meaning.*

@AndroidEntryPoint
class DisplayMeaningFragment : Fragment(R.layout.fragment_display_meaning) {

    private val dictionaryViewModel: DictionaryViewModel by viewModels()

    private var receivedWord: Word? = null
    private var mAdapter: DisplayMeaningAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = DisplayMeaningAdapter()
        getMessage()

        closeDisplayMeaningFrag.setOnClickListener {
            requireActivity().onBackPressed()
        }

        displayMeaningFragFAB.setOnClickListener {

            changeBookmarkState(receivedWord)
        }
    }

    private fun changeBookmarkState(word: Word?) {

        try {

            word?.let {

                it.isBookMarked = if (it.isBookMarked == getString(R.string.t)) {

                    displayMeaningFragFAB.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_bookmark_border_24))
                    getString(R.string.f)
                } else {

                    displayMeaningFragFAB.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_bookmark_24))
                    getString(R.string.t)
                }

                dictionaryViewModel.insertWord(it)
                Log.d("DisplayMeaningFragment", "Bookmark State Changed..")
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
                    "${receivedWord.word} |${receivedWord.pronunciation}|"
            }

            if (receivedWord.isBookMarked == getString(R.string.t)) {
                displayMeaningFragFAB.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_bookmark_24))
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