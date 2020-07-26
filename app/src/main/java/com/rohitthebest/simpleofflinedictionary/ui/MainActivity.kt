package com.rohitthebest.simpleofflinedictionary.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.rohitthebest.simpleofflinedictionary.R
import com.rohitthebest.simpleofflinedictionary.database.model.Word
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var wordList: ArrayList<Word>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordList = ArrayList()

        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.homeFragment -> showBottomNav()
                R.id.savedFragment -> showBottomNav()
                R.id.recentFragment -> showBottomNav()
                R.id.findOnlineFragment -> showBottomNav()
                else -> hideBottomNav()
            }

        }

/*
        try {

            val inputStream = assets.open("dictionary_in_txt.txt")
            val lineList = mutableListOf<String>()
            inputStream.bufferedReader().forEachLine { it ->

                lineList.add(it)
                */
/*  lineList.forEach { it1 ->
                      Log.d("test", ">  $it1")
                  }*//*

            }

            val gson = Gson()
            val type = object : TypeToken<Word>() {}.type
            var str = ""

            for ((j, i) in lineList.withIndex()) {

                val json = gson.fromJson<Word>(i, type)
                wordList!!.add(json)

                str += "$j -> ${json.word}\n"
            }
            */
/*  for (i in wordList!!) {

                str += "${i.definitions?.get(0)?.definition}\n"
            }*//*


            textView.text = str

        } catch (e: Exception) {
            e.printStackTrace()
        }
*/
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
    }

}