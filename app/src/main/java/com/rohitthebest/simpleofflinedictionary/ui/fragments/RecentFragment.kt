package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.adapters.HomeRVAdapter
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import com.rohitthebest.simpleofflinedictionary.ui.viewModels.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recent.*
import kotlinx.coroutines.*

@AndroidEntryPoint
class RecentFragment : Fragment(R.layout.fragment_recent), HomeRVAdapter.OnItemClickListener {

    private val dictionaryViewModel: DictionaryViewModel by viewModels()
    private var mAdapter: HomeRVAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = HomeRVAdapter()

        GlobalScope.launch {
            delay(150)

            withContext(Dispatchers.Main) {
                getAllRecentList()
            }
        }

        recentFragDeleteBtn.setOnClickListener {

            removeWordsFromRecent()
        }
    }

    private fun removeWordsFromRecent() {

        dictionaryViewModel.getWordsByRecent(getString(R.string.t))
            .observe(viewLifecycleOwner, Observer {

                if(it.isNotEmpty()){

                    for(word in it){

                        word.isInRecent = getString(R.string.f)
                        dictionaryViewModel.insertWord(word)
                    }
                }
            })

    }

    private fun getAllRecentList() {

        dictionaryViewModel.getWordsByRecent(getString(R.string.t))
            .observe(viewLifecycleOwner, Observer {

                try {
                    setUpRecyclerView(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
    }

    private fun setUpRecyclerView(wordList: List<Word>?) {

        try {

            wordList?.let {

                mAdapter?.submitList(it)

                recentFragRV?.apply {

                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = mAdapter
                }
                mAdapter?.setOnClickListener(this)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemClick(word: Word) {

        val gson = Gson()
        val message = gson.toJson(word)

        val action = RecentFragmentDirections.actionRecentFragmentToDisplayMeaningFragment(message)
        findNavController().navigate(action)

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
                Log.d("RecentFragment", "Bookmark State Changed...")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}