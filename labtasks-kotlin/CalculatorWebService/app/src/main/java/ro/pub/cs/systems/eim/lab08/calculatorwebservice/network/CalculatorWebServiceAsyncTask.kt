package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network

import android.os.AsyncTask
import android.util.Log
import android.widget.TextView

class CalculatorWebServiceAsyncTask(private val resultTextView: TextView) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String? {
        val operator1 = params[0]
        val operator2 = params[1]
        val operation = params[2]
        val method = params[3]?.toInt() ?: 0

        // TODO exercise 4
        // signal missing values through error messages

        // create an instance of a okhttp object

        // get method used for sending request from methodsSpinner

        // 1. GET
        // a) build the URL into a Get object (append the operators / operations to the Internet address)
        // b) create an instance of a HttpUrl.Builder object
        // c) execute the request, thus generating the result

        // 2. POST
        // a) build the URL into a PostBody object
        // b) create an instance of a RequestBuilder object
        // c) execute the request, thus generating the result

        return null
    }

    override fun onPostExecute(result: String?) {
        // display the result in resultTextView
    }
}

