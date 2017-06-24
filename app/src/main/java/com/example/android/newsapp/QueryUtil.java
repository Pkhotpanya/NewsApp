package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class QueryUtil {

    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";
    private static final String SECTION_ID = "sectionName";
    private static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final String WEB_TITLE = "webTitle";
    private static final String WEB_URL = "webUrl";

    public QueryUtil() {
    }

    public static List<NewsStory> extractNewsStory(String url) {
        return fetchNewsStoryData(url);
    }

    public static ArrayList<NewsStory> fetchNewsStoryData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Error closing input stream", e);
        }

        ArrayList<NewsStory> newsStories = extractFeatureFromJson(jsonResponse);

        return newsStories;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("QueryUtils", "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<NewsStory> extractFeatureFromJson(String newStoryJSON) {
        if (TextUtils.isEmpty(newStoryJSON)) {
            return null;
        }

        ArrayList<NewsStory> newsStories = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(newStoryJSON);
            JSONObject responseObject = jsonObject.optJSONObject(RESPONSE);
            JSONArray resultsArray = responseObject.optJSONArray(RESULTS);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject temp = resultsArray.optJSONObject(i);
                String title = temp.getString(WEB_TITLE);
                String section = temp.getString(SECTION_ID);
                String date = temp.getString(WEB_PUBLICATION_DATE);
                String url = temp.getString(WEB_URL);
                newsStories.add(new NewsStory(title, section, date, url));
            }
            return newsStories;

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return null;
    }

}
