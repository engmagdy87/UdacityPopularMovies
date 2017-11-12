package com.example.mm.popularmovies.networkhelpers;

import android.util.Log;

import com.example.mm.popularmovies.MainActivity;
import com.example.mm.popularmovies.MovieDetail;
import com.example.mm.popularmovies.javaclasses.MovieInfo;
import com.example.mm.popularmovies.javaclasses.MovieReviews;
import com.example.mm.popularmovies.javaclasses.MovieTrailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MM on 10/8/2017.
 */

public class MovieJsonUtils {
    public static ArrayList<MovieInfo> getSimpleMovieStringsFromJson(MainActivity context, String movieJsonStr)
            throws JSONException {

        String[] parsedMovieData = null;
        ArrayList<MovieInfo> moviesInfo = new ArrayList<MovieInfo>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        for (int i = 0; i < movieArray.length(); i++) {
            String id = String.valueOf(movieArray.getJSONObject(i).getInt("id"));
            String title = movieArray.getJSONObject(i).getString("title");
            String poster_Url = "http://image.tmdb.org/t/p/w185" + movieArray.getJSONObject(i).getString("poster_path");
            String overview = movieArray.getJSONObject(i).getString("overview");
            String vote_average = movieArray.getJSONObject(i).getString("vote_average");
            String release_date = movieArray.getJSONObject(i).getString("release_date");

            moviesInfo.add(new MovieInfo(id, title, poster_Url,overview,vote_average,release_date));
        }

        return moviesInfo;
    }
    public static ArrayList<MovieTrailers> getMovieTrailersStringsFromJson(MovieDetail context, String movieJsonStr)
            throws JSONException {

        ArrayList<MovieTrailers> moviesInfo = new ArrayList<MovieTrailers>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");
        Log.v("tag","Length " + movieArray.length());
        for (int i = 0; i < movieArray.length(); i++) {
            String key = movieArray.getJSONObject(i).getString("key");
            String name = movieArray.getJSONObject(i).getString("name");
            Log.v("tag","MY Trailers " + key + " " + name);
            moviesInfo.add(new MovieTrailers(key, name));
        }

        return moviesInfo;
    }
    public static ArrayList<MovieReviews> getMovieReviewsStringsFromJson(MovieDetail context, String movieJsonStr)
            throws JSONException {

        ArrayList<MovieReviews> moviesInfo = new ArrayList<MovieReviews>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        for (int i = 0; i < movieArray.length(); i++) {
            String author = movieArray.getJSONObject(i).getString("author");
            String content = movieArray.getJSONObject(i).getString("content");
            moviesInfo.add(new MovieReviews(author, content));
        }

        return moviesInfo;
    }

}
