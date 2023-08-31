package com.fidenz.android_boilerplate.google_location;

import android.os.AsyncTask;

import com.fidenz.android_boilerplate.listeners.TaskCompleteListeners;
import com.fidenz.android_boilerplate.utility.LogUtility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchURL extends AsyncTask<String, Void, String> {

    public static String distance;
    public static String distanceInDouble;

    TaskCompleteListeners googleDirectionService;
    String directionMode = "driving";



    public FetchURL(TaskCompleteListeners googleDirectionService) {
        this.googleDirectionService = googleDirectionService;
    }

    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
            LogUtility.debug("mylog", "Background task data " + data.toString());
        } catch (Exception e) {
            LogUtility.debug("mylog Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(googleDirectionService, directionMode);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            LogUtility.debug("mylog", "Downloaded URL: " + data.toString());
            distance = new JSONObject(data).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
            distanceInDouble = new JSONObject(data).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("value");




            br.close();
        } catch (Exception e) {
            LogUtility.debug("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}