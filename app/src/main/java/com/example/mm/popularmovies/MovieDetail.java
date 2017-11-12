package com.example.mm.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mm.popularmovies.adapters.MovieReviewsAdapter;
import com.example.mm.popularmovies.adapters.MovieTrailersAdapter;
import com.example.mm.popularmovies.javaclasses.MovieReviews;
import com.example.mm.popularmovies.javaclasses.MovieTrailers;
import com.example.mm.popularmovies.networkhelpers.MovieJsonUtils;
import com.example.mm.popularmovies.networkhelpers.NetworkUtils;
import com.example.mm.popularmovies.database.DbContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity implements MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler,MovieReviewsAdapter.MovieReviewAdapterOnClickHandler {
    Context context = this;
    private static final String ONSAVEINSTANCESTATE_TRAILERS_KEY = "trailers";
    private static final String ONSAVEINSTANCESTATE_REVIEWS_KEY = "reviews";
    private static final String STAR_ON = "star_on";
    private static final String STAR_OFF = "star_off";
    ArrayList<MovieTrailers> gMovieTrailers;
    ArrayList<MovieReviews> gMovieReviews;

    private String id;
    String MovieTitle,MovieReleaseDate,MovieRating,MovieDesc,MoviePoster;
    private TextView title;
    private ImageView poster;
    private TextView release_date;
    private TextView rating;
    private TextView description;
    private ProgressBar loadingIndicator;
    private RecyclerView trailersRecycView;
    private RecyclerView reviewsRecycView;
    private MovieTrailersAdapter trailersAdapter;
    private MovieReviewsAdapter reviewsAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        trailersAdapter = new MovieTrailersAdapter(this);
        reviewsAdapter = new MovieReviewsAdapter(this);
        title = (TextView) findViewById(R.id.title);
        poster = (ImageView) findViewById(R.id.poster);
        release_date = (TextView) findViewById(R.id.release_date);
        rating = (TextView) findViewById(R.id.rate);
        description = (TextView) findViewById(R.id.description);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        getLoaderManager().initLoader(TASK_LOADER_ID, null, callbacks);


        trailersRecycView = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(context);
        trailersRecycView.setLayoutManager(layoutManagerTrailers);
        trailersRecycView.setHasFixedSize(true);
        trailersRecycView.setAdapter(trailersAdapter);

        reviewsRecycView = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(context);
        reviewsRecycView.setLayoutManager(layoutManagerReviews);
        reviewsRecycView.setHasFixedSize(true);
        reviewsRecycView.setAdapter(reviewsAdapter);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ONSAVEINSTANCESTATE_REVIEWS_KEY) && savedInstanceState.containsKey(ONSAVEINSTANCESTATE_TRAILERS_KEY)) {

                gMovieTrailers = savedInstanceState.getParcelableArrayList(ONSAVEINSTANCESTATE_TRAILERS_KEY);
                gMovieReviews = savedInstanceState.getParcelableArrayList(ONSAVEINSTANCESTATE_REVIEWS_KEY);
                trailersAdapter.setMovieTrailer(gMovieTrailers);
                reviewsAdapter.setMovieReview(gMovieReviews);
                id = savedInstanceState.getString("id");

                MovieTitle = savedInstanceState.getString("title");
                title.setText(MovieTitle);

                MovieReleaseDate = savedInstanceState.getString("release_date");
                release_date.setText(MovieReleaseDate.substring(0, MovieReleaseDate.indexOf("-")));

                MovieRating = savedInstanceState.getString("rate");
                rating.setText(MovieRating+"/10");

                MovieDesc = savedInstanceState.getString("description");
                description.setText(MovieDesc);

                MoviePoster = savedInstanceState.getString("poster");
                Picasso.with(context).load(MoviePoster).into(poster);
            }

        } else {

            Intent myIntent = getIntent();

            Bundle extras = myIntent.getExtras();
            if (extras != null) {
                if (extras.containsKey("id")) {
                    this.id = myIntent.getStringExtra("id");
                }
                if (extras.containsKey("title")) {
                    MovieTitle = myIntent.getStringExtra("title");
                    title.setText(MovieTitle);
                }
                if (extras.containsKey("release_date")) {
                    MovieReleaseDate = myIntent.getStringExtra("release_date");
                    release_date.setText(MovieReleaseDate.substring(0, MovieReleaseDate.indexOf("-")));
                }
                if (extras.containsKey("rate")) {
                    MovieRating = myIntent.getStringExtra("rate");
                    rating.setText(MovieRating+"/10");
                }
                if (extras.containsKey("description")) {
                    MovieDesc = myIntent.getStringExtra("description");
                    description.setText(MovieDesc);
                }
                if (extras.containsKey("poster")) {
                    MoviePoster = myIntent.getStringExtra("poster");
                    Picasso.with(context).load(MoviePoster).into(poster);
                }
            }
            loadMovieVideos("videos", id);
            loadMovieReviews("reviews", id);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id",id);
        outState.putString("title",MovieTitle);
        outState.putString("release_date",MovieReleaseDate);
        outState.putString("rate",MovieRating);
        outState.putString("description",MovieDesc);
        outState.putString("poster",MoviePoster);
        outState.putParcelableArrayList(ONSAVEINSTANCESTATE_TRAILERS_KEY,gMovieTrailers);
        outState.putParcelableArrayList(ONSAVEINSTANCESTATE_REVIEWS_KEY,gMovieReviews);
    }



    private void loadMovieVideos (String videosAndReviews, String id){
        new FetchVideos().execute(videosAndReviews, id);
    }
    private void loadMovieReviews (String videosAndReviews, String id){
        new FetchReviews().execute(videosAndReviews, id);
    }

    @Override
    public void onClick(MovieTrailers movie) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=" + movie.getId()));
        if(youtubeIntent.resolveActivity(getPackageManager()) != null)
            startActivity(youtubeIntent);
    }

    @Override
    public void onClick(MovieReviews movie) {

    }


    public class FetchVideos extends AsyncTask<Object, Void, ArrayList<MovieTrailers>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieTrailers> doInBackground(Object... objects) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            boolean internetState = (netInfo != null && netInfo.isConnected());

            if (objects.length == 0 || !internetState) {
                return null;
            }

            String videosAndReviews = (String) objects[0];
            String movieId  = (String) objects[1];

            URL movieRequestUrl = NetworkUtils.buildUrl(null,movieId,videosAndReviews);
            String jsonMovieResponse = null;

            try {
                jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            ArrayList<MovieTrailers> simpleJsonMovieData = null;
            try {
                simpleJsonMovieData = MovieJsonUtils
                            .getMovieTrailersStringsFromJson(MovieDetail.this, jsonMovieResponse);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return simpleJsonMovieData;
        }

        @Override
        protected void onPostExecute(final ArrayList<MovieTrailers> movieTrailers) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            gMovieTrailers = movieTrailers;
            trailersAdapter.setMovieTrailer(movieTrailers);
        }
    }


    public class FetchReviews extends AsyncTask<Object, Void, ArrayList<MovieReviews>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieReviews> doInBackground(Object... objects) {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            boolean internetState = (netInfo != null && netInfo.isConnected());

            if (objects.length == 0 || !internetState) {
                return null;
            }

            String videosAndReviews = (String) objects[0];
            String movieId  = (String) objects[1];

            URL movieRequestUrl = NetworkUtils.buildUrl(null,movieId,videosAndReviews);
            String jsonMovieResponse = null;

            try {
                jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            ArrayList<MovieReviews> simpleJsonMovieData = null;
            try {
                simpleJsonMovieData = MovieJsonUtils
                        .getMovieReviewsStringsFromJson(MovieDetail.this, jsonMovieResponse);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return simpleJsonMovieData;
        }

        @Override
        protected void onPostExecute(final ArrayList<MovieReviews> movieReviews) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            gMovieReviews = movieReviews;
            reviewsAdapter.setMovieReview(movieReviews);
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this, "An internet connection error. Please try again",
                Toast.LENGTH_LONG).show();
    }

    public void onClickAddMovieToFav(View view){
        ImageView starIcon = (ImageView) findViewById(R.id.star_icon);
        if(starIcon.getTag().toString().equals("star_off")){
            ContentValues contentValues = new ContentValues();

            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_ID,Integer.parseInt(this.id));
            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_TITLE,this.MovieTitle);
            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_DESC,this.MovieDesc);
            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_POSTER_PATH,this.MoviePoster);
            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_VOTE_AVG,Float.parseFloat(this.MovieRating));
            contentValues.put(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_REL_DATE,this.MovieReleaseDate);

            Uri uri = getContentResolver().insert(DbContract.FavoriteMoviesTable.CONTENT_URI, contentValues);

            if(uri != null) {
                Toast.makeText(getBaseContext(), "Successful adding movie to favorite", Toast.LENGTH_LONG).show();
            }

            starIcon.setTag(STAR_ON);
            starIcon.setImageResource(R.drawable.star_on);
        } else {
            getContentResolver().delete(DbContract.FavoriteMoviesTable.CONTENT_URI, null, new String[]{id});
            starIcon.setTag(STAR_OFF);
            starIcon.setImageResource(R.drawable.star_off);
        }

    }

    LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new AsyncTaskLoader<Cursor>(MovieDetail.this) {

                Cursor cursor = null;

                @Override
                protected void onStartLoading() {
                    if (cursor != null) {
                        deliverResult(cursor);
                    } else {
                        forceLoad();
                    }
                }
                @Override
                public Cursor loadInBackground() {
                    try {
                        return getContentResolver().query(DbContract.FavoriteMoviesTable.CONTENT_URI,
                                null,"mdb_movie_id ="+id
                                , null
                                ,null);

                    } catch (Exception e) {
                        Log.e(TAG, "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }
                @Override
                public void deliverResult(Cursor data) {
                    cursor = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader loader, Cursor cursor) {
            ImageView starIcon = (ImageView) findViewById(R.id.star_icon);

            if(cursor.getCount() == 1)
            {
                starIcon.setTag(STAR_ON);
                starIcon.setImageResource(R.drawable.star_on);
            }
        }


        @Override
        public void onLoaderReset(Loader loader) {

        }
    };
}
