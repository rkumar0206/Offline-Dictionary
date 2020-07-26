package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.adapters.HomeRVAdapter
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import com.rohitthebest.simpleofflinedictionary.others.Constants.SHARED_PREFS
import com.rohitthebest.simpleofflinedictionary.others.Functions.Companion.closeKeyboard
import com.rohitthebest.simpleofflinedictionary.ui.viewModels.DictionaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_display_meaning.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), HomeRVAdapter.OnItemClickListener {

    private val TAG = "HomeFragment"
    private val dictionaryViewModel: DictionaryViewModel by viewModels()
    private var flag = false

    private var mAdapter: HomeRVAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = HomeRVAdapter()
        loadData()

        showProgressBar()
        if (!flag) {
            addWordsToDataBase()
        }else{

            GlobalScope.launch {
                delay(200)
                withContext(Dispatchers.Main) {

                    getAllWordsList()
                }
            }
        }

        homeFragClearEditText.setOnClickListener {

            homeFragSearchET?.setText("")
        }
    }

    private fun getAllWordsList() {

        try {
            dictionaryViewModel.getAllWords().observe(viewLifecycleOwner, Observer {

                if (it.isNotEmpty()) {

                    homeFragSearchET.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {

                            if (s?.isEmpty()!!) {
                                setUpRecyclerView(it)
                            } else {

                                val filteredList = it.filter { word ->

                                    word.word?.toLowerCase(Locale.ROOT)
                                        ?.startsWith(s.toString().trim().toLowerCase(Locale.ROOT))!!
                                }

                                setUpRecyclerView(filteredList)
                            }

                        }
                    })
                    setUpRecyclerView(it)

                } else {

                    try {
                        setUpRecyclerView(it)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })


        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        hideProgressBar()
    }

    private fun setUpRecyclerView(wordList: List<Word>?) {

        try {

            mAdapter?.submitList(wordList)

            homeFragRV?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mAdapter
            }
            mAdapter?.setOnClickListener(this)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemClick(word: Word) {

        val gson = Gson()
        val message = gson.toJson(word)

        val action = HomeFragmentDirections.actionHomeFragmentToDisplayMeaningFragment(message)
        findNavController().navigate(action)

        CoroutineScope(Dispatchers.IO).launch {
            closeKeyboard(requireActivity())
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

    private fun addWordsToDataBase() {

        try {

            val inputStream = requireActivity().assets.open("dictionary_in_txt.txt")
            val lineList = mutableListOf<String>()

            try {
                Log.d(TAG, "Deleting All List")
                dictionaryViewModel.deleteAll()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            inputStream.bufferedReader().forEachLine {
                lineList.add(it)
            }

            val gson = Gson()
            val type = object : TypeToken<Word>() {}.type

            for ((i, word) in lineList.withIndex()) {

                val json = gson.fromJson<Word>(word, type)

                json.id = json.word
                json.isBookMarked = getString(R.string.f)
                dictionaryViewModel.insertWord(json)

                if (i == lineList.size - 1) {
                    flag = true
                    saveData()
                    hideProgressBar()
                    getAllWordsList()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveData() {

        Log.d(TAG, "Saving Data")

        val sharedPreference =
            requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        val editor = sharedPreference.edit()
        editor.putBoolean(getString(R.string.insert_data_key), flag)

        editor.apply()
    }

    private fun loadData() {

        Log.d(TAG, "Loading Data")

        val sharedPreference =
            requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        try {

            flag = sharedPreference.getBoolean(getString(R.string.insert_data_key), false)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showProgressBar() {
        try {
            homeFragProgressBar.visibility = View.VISIBLE
            homeFragRV.visibility = View.GONE
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun hideProgressBar() {
        try {
            homeFragProgressBar.visibility = View.GONE
            homeFragRV.visibility = View.VISIBLE
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}