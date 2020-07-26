package com.rohitthebest.simpleofflinedictionary.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.others.Functions.Companion.openLinkInBrowser
import kotlinx.android.synthetic.main.fragment_find_online.*

class FindOnlineFragment : Fragment(R.layout.fragment_find_online), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findOnlineFragSearchBtn.setOnClickListener(this)

        findOnlineFragSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                searchOnWeb(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            findOnlineFragSearchBtn.id -> {

                if (findOnlineFragSV.query.isNotEmpty()) {

                    searchOnWeb(findOnlineFragSV.query.toString().trim())
                }
            }
        }
    }

    private fun searchOnWeb(word: String?) {

        openLinkInBrowser("$word meaning", requireContext())
    }

}