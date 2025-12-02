package ro.pub.cs.systems.eim.ocw.displayer.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ro.pub.cs.systems.eim.lab08.ocwcoursesdisplayer.Constants;


public class OCWCoursesDisplayerAsyncTask extends AsyncTask<String, Void, String> {

    private TextView ocwCoursesDisplayerTextView;

    public OCWCoursesDisplayerAsyncTask(TextView ocwCoursesDisplayerTextView) {
        this.ocwCoursesDisplayerTextView = ocwCoursesDisplayerTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        // 1. Create an OkHttpClient instance
        final OkHttpClient httpClient = new OkHttpClient();

        // 2. Build the Request
        Request httpRequest = new Request.Builder()
                .url(Constants.OCW_COURSES_WEB_SERVICE_ADDRESS)
                .build();

        try {
            // 3. Execute the request and get the Response
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            ResponseBody body = httpResponse.body();
            if (body != null) {
                // 4. Return the response body as a string
                return body.string();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null || result.isEmpty()) {
            Log.e(Constants.TAG, "Result is null or empty");
            ocwCoursesDisplayerTextView.setText(""); // Clear the text view on error
            return;
        }

        // The JSON parsing logic remains the same
        StringBuilder stringBuilder = new StringBuilder();
        try {
            JSONObject content = new JSONObject(result);
            JSONArray courses = content.getJSONArray(Constants.COURSES);
            for (int i = 0; i < courses.length(); i++) {
                JSONObject course = courses.getJSONObject(i);
                stringBuilder.append(course.getString(Constants.NAME));
                stringBuilder.append(" (");
                stringBuilder.append(course.getString(Constants.ID));
                stringBuilder.append(")");
                stringBuilder.append("\n");
            }
        } catch (JSONException jsonException) {
            Log.e(Constants.TAG, "An exception has occurred: " + jsonException.getMessage());
        }
        ocwCoursesDisplayerTextView.setText(stringBuilder.toString());
    }
}
