package ro.pub.cs.systems.eim.lab07.earthquakelister.network;

import android.os.AsyncTask;
import android.util.Log;import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ro.pub.cs.systems.eim.lab07.earthquakelister.controller.EarthquakeInformationAdapter;
import ro.pub.cs.systems.eim.lab07.earthquakelister.general.Constants;
import ro.pub.cs.systems.eim.lab07.earthquakelister.model.EarthquakeInformation;

public class EarthquakeListerAsyncTask extends AsyncTask<String, Void, List<EarthquakeInformation>> {

    private ListView earthquakeListView;

    public EarthquakeListerAsyncTask(ListView earthquakeListView) {
        this.earthquakeListView = earthquakeListView;
    }

    @Override
    protected List<EarthquakeInformation> doInBackground(String... params) {
        // 1. Create an OkHttpClient instance
        final OkHttpClient httpClient = new OkHttpClient();

        // 2. Construct the URL
        String url = Constants.EARTHQUAKE_LISTER_WEB_SERVICE_INTERNET_ADDRESS +
                Constants.NORTH + params[Constants.NORTH_INDEX] +
                "&" + Constants.SOUTH + params[Constants.SOUTH_INDEX] +
                "&" + Constants.EAST + params[Constants.EAST_INDEX] +
                "&" + Constants.WEST + params[Constants.WEST_INDEX] +
                "&" + Constants.CREDENTIALS;
        Log.d(Constants.TAG, "url=" + url);

        try {
            // 3. Build the Request
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .build();

            // 4. Execute the request and get the Response
            Response httpResponse = httpClient.newCall(httpRequest).execute();
            String content = null;
            ResponseBody body = httpResponse.body();
            if (body != null) {
                content = body.string();
            }

            Log.d(Constants.TAG, "content=" + content);
            List<EarthquakeInformation> earthquakeInformationList = new ArrayList<>();
            if (content != null) {
                JSONObject result = new JSONObject(content);
                JSONArray jsonArray = result.getJSONArray(Constants.EARTHQUAKES);
                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    earthquakeInformationList.add(new EarthquakeInformation(
                            jsonObject.getDouble(Constants.LATITUDE),
                            jsonObject.getDouble(Constants.LONGITUDE),
                            jsonObject.getDouble(Constants.MAGNITUDE),
                            jsonObject.getDouble(Constants.DEPTH),
                            jsonObject.getString(Constants.SOURCE),
                            jsonObject.getString(Constants.DATE_AND_TIME)
                    ));
                }
            }
            return earthquakeInformationList;
        } catch (JSONException | IOException e) {
            Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void onPostExecute(List<EarthquakeInformation> earthquakeInformationList) {
        // This remains the same
        if (earthquakeInformationList != null) {
            earthquakeListView.setAdapter(new EarthquakeInformationAdapter(
                    earthquakeListView.getContext(),
                    earthquakeInformationList
            ));
        }
    }
}
