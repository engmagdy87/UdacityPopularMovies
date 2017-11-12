package com.example.mm.popularmovies.networkhelpers;

import android.net.Uri;
import android.util.Log;

import com.example.mm.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by MM on 10/7/2017.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String sortByPop = "popular";
    private static final String sortByTopR = "top_rated";
    private static final String getReviews = "/reviews";
    private static final String getVideos = "/videos";
    private static final String STATIC_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String api_key = "api_key";

    public static URL buildUrl(String sortBy, String id, String richDetails) {
        String MOVIE_BASE_URL = null;
        if(sortBy == "popular" && id == null && richDetails == null){
            MOVIE_BASE_URL = STATIC_BASE_URL + sortByPop;
        }else if (sortBy == "top rated" && id == null && richDetails == null){
            MOVIE_BASE_URL = STATIC_BASE_URL + sortByTopR;
        } else if (sortBy == null && id != null && richDetails == "videos"){
            MOVIE_BASE_URL = STATIC_BASE_URL + id + getVideos;
        } else if (sortBy == null && id != null && richDetails == "reviews"){
            MOVIE_BASE_URL = STATIC_BASE_URL + id + getReviews;
        }


        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(api_key, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
