package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.adapters.HomeRVAdapter
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import com.rohitthebest.simpleofflinedictionary.others.Functions
import com.rohitthebest.simpleofflinedictionary.ui.viewModels.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class SavedFragment : Fragment(R.layout.fragment_saved), HomeRVAdapter.OnItemClickListener {

    private val dictionaryViewModel: DictionaryViewModel by viewModels()
    private var mAdapter: HomeRVAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = HomeRVAdapter()

        GlobalScope.launch {

            delay(150)
            withContext(Dispatchers.Main) {
                getAllBookmarkList()
            }
        }

        closeSavedFrag.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getAllBookmarkList() {

        dictionaryViewModel.getWordsByBookmark(getString(R.string.t))
            .observe(viewLifecycleOwner, Observer {

                if (it.isNotEmpty()) {

                    hideNoBookmarkTV()

                    savedFragSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {

                            newText?.let { newTxt ->

                                if (newTxt.isEmpty()) {
                                    setUpRecyclerView(it)
                                } else {

                                    val filteredList = it.filter { word ->

                                        word.word?.toLowerCase(Locale.ROOT)
                                            ?.startsWith(newTxt.trim().toLowerCase(Locale.ROOT))!!
                                    }
                                    setUpRecyclerView(filteredList)
                                }
                            }
                            return false
                        }
                    })

                    setUpRecyclerView(it)
                } else {

                    showNoBookmarkTV()
                    try {
                        setUpRecyclerView(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun setUpRecyclerView(wordList: List<Word>?) {

        try {

            mAdapter?.submitList(wordList)

            savedFragRV.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mAdapter
            }

            mAdapter?.setOnClickListener(this)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showNoBookmarkTV() {

        savedFragNoBookmarksAddedTV.visibility = View.VISIBLE
        savedFragRV.visibility = View.GONE
    }

    private fun hideNoBookmarkTV() {

        savedFragNoBookmarksAddedTV.visibility = View.GONE
        savedFragRV.visibility = View.VISIBLE
    }

    override fun onItemClick(word: Word) {

        val gson = Gson()
        val message = gson.toJson(word)

        val action = SavedFragmentDirections.actionSavedFragmentToDisplayMeaningFragment(message)
        findNavController().navigate(action)

        CoroutineScope(Dispatchers.IO).launch {
            Functions.closeKeyboard(requireActivity())
        }
    }

    override fun onBookmarkBtnClicked(word: Word?) {

        try {

            word?.let {

                it.isBookMarked = if (it.isBookMarked == getString(R.string.t)) {
                    getString(R.string.f)
                } else {
                    getString(R.string.t)
                }
                dictionaryViewModel.insertWord(it)
                Log.d("DisplayMeaningFragment", "Bookmark State Changed..")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}