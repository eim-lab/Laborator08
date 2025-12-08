package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import ro.pub.cs.systems.eim.lab08.calculatorwebservice.data.OperationsDatabaseHelper;
import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private final WeakReference<TextView> resultTextViewReference;
    private final WeakReference<Context> contextReference;

    public CalculatorWebServiceAsyncTask(TextView resultTextView, Context context) {
        this.resultTextViewReference = new WeakReference<>(resultTextView);
        this.contextReference = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);
        String methodName = (method == Constants.GET_OPERATION) ? "GET" : "POST";

        if (operator1 == null || operator1.isEmpty() || operator2 == null || operator2.isEmpty()) {
            return Constants.ERROR_MESSAGE_EMPTY;
        }

        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();
        Request request = null;

        try {
            switch(method) {
                case Constants.GET_OPERATION:
                    String getstr = Constants.GET_WEB_SERVICE_ADDRESS + "?"    +
                            Constants.OPERATION_ATTRIBUTE + "=" + operation + "&" +
                            Constants.OPERATOR1_ATTRIBUTE + "=" + operator1 + "&" +
                            Constants.OPERATOR2_ATTRIBUTE + "=" + operator2;
                    request = new Request.Builder()
                            .url(getstr)
                            .build();
                    break;
                case Constants.POST_OPERATION:
                    RequestBody postBody = new FormBody.Builder()
                            .add(Constants.OPERATION_ATTRIBUTE, operation)
                            .add(Constants.OPERATOR1_ATTRIBUTE, operator1)
                            .add(Constants.OPERATOR2_ATTRIBUTE, operator2)
                            .build();

                    request = new Request.Builder()
                            .url(Constants.POST_WEB_SERVICE_ADDRESS)
                            .post(postBody)
                            .build();
                    break;
            }

            if (request == null) {
                return "Error: Invalid method selected.";
            }

            // Execute the request and get the response
            Response response = client.newCall(request).execute();
            String result;
            if (response.isSuccessful() && response.body() != null) {
                result = response.body().string();
            } else {
                result = "Error: " + response.code() + " " + response.message();
            }

            // Save operation to database
            Context context = contextReference.get();
            if (context != null) {
                try {
                    OperationsDatabaseHelper dbHelper = new OperationsDatabaseHelper(context);
                    dbHelper.insertOperation(operator1, operator2, operation, methodName, result);
                } catch (Exception e) {
                    Log.e(Constants.TAG, "Failed to save operation to database: " + e.getMessage());
                }
            }

            return result;

        } catch (IOException e) {
            Log.e(Constants.TAG, "OkHttp request failed: " + e.getMessage());
            String errorMessage = "Error: " + e.getMessage();
            
            // Save error operation to database
            Context context = contextReference.get();
            if (context != null) {
                try {
                    OperationsDatabaseHelper dbHelper = new OperationsDatabaseHelper(context);
                    dbHelper.insertOperation(operator1, operator2, operation, methodName, errorMessage);
                } catch (Exception dbException) {
                    Log.e(Constants.TAG, "Failed to save operation to database: " + dbException.getMessage());
                }
            }
            
            return errorMessage;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Use the WeakReference to safely access the TextView
        TextView resultTextView = resultTextViewReference.get();
        if (resultTextView != null) {
            resultTextView.setText(result);
        }
    }
}
