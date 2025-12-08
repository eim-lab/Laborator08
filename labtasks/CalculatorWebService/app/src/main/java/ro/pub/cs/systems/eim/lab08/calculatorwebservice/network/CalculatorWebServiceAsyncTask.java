package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);

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

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // display the result in resultTextView
    }

}
