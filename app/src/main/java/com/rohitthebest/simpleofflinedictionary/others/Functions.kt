package com.rohitthebest.simpleofflinedictionary.others

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.rohitthebest.simpleofflinedictionary.others.Constants.NO_INTERNET_MESSAGE
import com.rohitthebest.simpleofflinedictionary.util.CheckNetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Functions {

    companion object {

        private const val TAG = "Functions"
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            try {
                Log.d(TAG, message)
                Toast.makeText(context, message, duration).show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun isInternetAvailable(context: Context): Boolean {

            return CheckNetworkConnection().isInternetAvailable(context)
        }

        fun checkUrl(url: String): String {

            var urll = ""
            try {
                if (url.startsWith("https://") || url.startsWith("http://")) {
                    urll = url
                } else if (url.isNotEmpty()) {
                    urll = "https://www.google.com/search?q=$url"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return urll

        }

        fun openLinkInBrowser(url: String?, context: Context) {

            if (isInternetAvailable(context)) {
                url?.let {

                    try {
                        Log.d(TAG, "Loading Url in default browser.")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(checkUrl(it)))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        showToast(context, e.message.toString())
                        e.printStackTrace()
                    }
                }
            } else {
                showToast(context, NO_INTERNET_MESSAGE)
            }
        }

        /*fun shareAsText(message: String?, subject: String?, context: Context) {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(intent, "Share Via"))

        }*/

        suspend fun closeKeyboard(activity: Activity) {
            try {
                withContext(Dispatchers.IO) {

                    val view = activity.currentFocus

                    if (view != null) {

                        val inputMethodManager =
                            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}