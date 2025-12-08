package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import java.lang.ref.WeakReference
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.data.OperationsDatabaseHelper
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants

class CalculatorWebServiceAsyncTask(
    resultTextView: TextView,
    context: Context
) : AsyncTask<String, Void, String>() {

    private val resultTextViewReference: WeakReference<TextView> = WeakReference(resultTextView)
    private val contextReference: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg params: String?): String? {
        val operator1 = params[0]
        val operator2 = params[1]
        val operation = params[2]
        val method = params[3]?.toInt() ?: 0
        val methodName = if (method == Constants.GET_OPERATION) "GET" else "POST"

        if (operator1.isNullOrEmpty() || operator2.isNullOrEmpty()) {
            return Constants.ERROR_MESSAGE_EMPTY
        }

        // Create an OkHttpClient instance
        val client = OkHttpClient()
        var request: Request? = null

        try {
            when (method) {
                Constants.GET_OPERATION -> {
                    val getStr = Constants.GET_WEB_SERVICE_ADDRESS + "?" +
                            Constants.OPERATION_ATTRIBUTE + "=" + operation + "&" +
                            Constants.OPERATOR1_ATTRIBUTE + "=" + operator1 + "&" +
                            Constants.OPERATOR2_ATTRIBUTE + "=" + operator2
                    request = Request.Builder()
                        .url(getStr)
                        .build()
                }
                Constants.POST_OPERATION -> {
                    val postBody: RequestBody = FormBody.Builder()
                        .add(Constants.OPERATION_ATTRIBUTE, operation.orEmpty())
                        .add(Constants.OPERATOR1_ATTRIBUTE, operator1.orEmpty())
                        .add(Constants.OPERATOR2_ATTRIBUTE, operator2.orEmpty())
                        .build()

                    request = Request.Builder()
                        .url(Constants.POST_WEB_SERVICE_ADDRESS)
                        .post(postBody)
                        .build()
                }
            }

            if (request == null) {
                return "Error: Invalid method selected."
            }

            // Execute the request and get the response
            val response: Response = client.newCall(request).execute()
            val result = if (response.isSuccessful && response.body != null) {
                response.body!!.string()
            } else {
                "Error: ${response.code} ${response.message}"
            }

            // Save operation to database
            val context = contextReference.get()
            if (context != null) {
                try {
                    val dbHelper = OperationsDatabaseHelper(context)
                    dbHelper.insertOperation(operator1.orEmpty(), operator2.orEmpty(), operation.orEmpty(), methodName, result)
                } catch (e: Exception) {
                    Log.e(Constants.TAG, "Failed to save operation to database: ${e.message}")
                }
            }

            return result

        } catch (e: Exception) {
            Log.e(Constants.TAG, "OkHttp request failed: ${e.message}")
            val errorMessage = "Error: ${e.message}"
            
            // Save error operation to database
            val context = contextReference.get()
            if (context != null) {
                try {
                    val dbHelper = OperationsDatabaseHelper(context)
                    dbHelper.insertOperation(operator1.orEmpty(), operator2.orEmpty(), operation.orEmpty(), methodName, errorMessage)
                } catch (dbException: Exception) {
                    Log.e(Constants.TAG, "Failed to save operation to database: ${dbException.message}")
                }
            }
            
            return errorMessage
        }
    }

    override fun onPostExecute(result: String?) {
        val resultTextView = resultTextViewReference.get()
        resultTextView?.text = result
    }
}

