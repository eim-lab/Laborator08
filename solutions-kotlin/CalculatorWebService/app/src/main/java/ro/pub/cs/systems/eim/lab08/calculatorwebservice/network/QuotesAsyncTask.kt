package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network

import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import java.lang.ref.WeakReference
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants

class QuotesAsyncTask(quoteTextView: TextView) : AsyncTask<Int, Void, String>() {

    private val quoteTextViewReference: WeakReference<TextView> = WeakReference(quoteTextView)

    override fun doInBackground(vararg params: Int?): String? {
        val quoteIndex = params[0] ?: 0
        val client = OkHttpClient()

        try {
            // Use skip parameter to get a different quote each time
            val url = "https://dummyjson.com/quotes?skip=$quoteIndex&limit=1"
            val request = Request.Builder()
                .url(url)
                .build()

            val response: Response = client.newCall(request).execute()
            
            if (response.isSuccessful && response.body != null) {
                val jsonResponse = response.body!!.string()
                val jsonObject = JSONObject(jsonResponse)
                val quotesArray = jsonObject.getJSONArray("quotes")
                
                if (quotesArray.length() > 0) {
                    val quote = quotesArray.getJSONObject(0)
                    val quoteText = quote.getString("quote")
                    val quoteAuthor = quote.getString("author")
                    return "\"$quoteText\"\n\n- $quoteAuthor"
                } else {
                    val fallbackRequest = Request.Builder()
                        .url("https://dummyjson.com/quotes?skip=0&limit=1")
                        .build()
                    val fallbackResponse: Response = client.newCall(fallbackRequest).execute()
                    if (fallbackResponse.isSuccessful && fallbackResponse.body != null) {
                        val fallbackJson = JSONObject(fallbackResponse.body!!.string())
                        val fallbackArray = fallbackJson.getJSONArray("quotes")
                        if (fallbackArray.length() > 0) {
                            val quote = fallbackArray.getJSONObject(0)
                            val quoteText = quote.getString("quote")
                            val quoteAuthor = quote.getString("author")
                            return "\"$quoteText\"\n\n- $quoteAuthor"
                        }
                    }
                    return "No quotes available"
                }
            } else {
                return "Error: ${response.code} ${response.message}"
            }

        } catch (e: Exception) {
            Log.e(Constants.TAG, "Quotes request failed: ${e.message}")
            return "Error: ${e.message}"
        }
    }

    override fun onPostExecute(result: String?) {
        val quoteTextView = quoteTextViewReference.get()
        quoteTextView?.text = result
    }
}

