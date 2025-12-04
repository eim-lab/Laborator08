package ro.pub.cs.systems.eim.lab08.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ro.pub.cs.systems.eim.lab08.calculatorwebservice.general.Constants;

public class QuotesAsyncTask extends AsyncTask<Integer, Void, String> {

    private final WeakReference<TextView> quoteTextViewReference;

    public QuotesAsyncTask(TextView quoteTextView) {
        this.quoteTextViewReference = new WeakReference<>(quoteTextView);
    }

    @Override
    protected String doInBackground(Integer... params) {
        int quoteIndex = params.length > 0 && params[0] != null ? params[0] : 0;
        OkHttpClient client = new OkHttpClient();

        try {
            // Use skip parameter to get a different quote each time
            String url = "https://dummyjson.com/quotes?skip=" + quoteIndex + "&limit=1";
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray quotesArray = jsonObject.getJSONArray("quotes");

                if (quotesArray.length() > 0) {
                    JSONObject quote = quotesArray.getJSONObject(0);
                    String quoteText = quote.getString("quote");
                    String quoteAuthor = quote.getString("author");
                    return "\"" + quoteText + "\"\n\n- " + quoteAuthor;
                } else {
                    // If we've reached the end, wrap around to the beginning
                    Request fallbackRequest = new Request.Builder()
                            .url("https://dummyjson.com/quotes?skip=0&limit=1")
                            .build();
                    Response fallbackResponse = client.newCall(fallbackRequest).execute();
                    if (fallbackResponse.isSuccessful() && fallbackResponse.body() != null) {
                        JSONObject fallbackJson = new JSONObject(fallbackResponse.body().string());
                        JSONArray fallbackArray = fallbackJson.getJSONArray("quotes");
                        if (fallbackArray.length() > 0) {
                            JSONObject quote = fallbackArray.getJSONObject(0);
                            String quoteText = quote.getString("quote");
                            String quoteAuthor = quote.getString("author");
                            return "\"" + quoteText + "\"\n\n- " + quoteAuthor;
                        }
                    }
                    return "No quotes available";
                }
            } else {
                return "Error: " + response.code() + " " + response.message();
            }

        } catch (IOException | JSONException e) {
            Log.e(Constants.TAG, "Quotes request failed: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        TextView quoteTextView = quoteTextViewReference.get();
        if (quoteTextView != null) {
            quoteTextView.setText(result);
        }
    }
}

