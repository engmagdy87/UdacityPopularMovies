package com.example.mm.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mm.popularmovies.adapters.MovieItemAdapter;
import com.example.mm.popularmovies.javaclasses.MovieInfo;
import com.example.mm.popularmovies.networkhelpers.MovieJsonUtils;
import com.example.mm.popularmovies.networkhelpers.NetworkUtils;
import com.example.mm.popularmovies.database.DbContract;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieItemAdapter.MovieItemAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private Context context = this;
    private RecyclerView recyclerView;

    private MovieItemAdapter movieAdapter = new MovieItemAdapter(this,  this);
    private ArrayList<MovieInfo> ONSAVEINSTANCESTATE_MOVIES = null;
    private ProgressBar loadingIndicator;
    private static final String ONSAVEINSTANCESTATE_KEY = "movies";
    private static String SELECTED_OPTION = "popular";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ONSAVEINSTANCESTATE_KEY,ONSAVEINSTANCESTATE_MOVIES);
        outState.putString("SELECTED_OPTION",SELECTED_OPTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        StaggeredGridLayoutManager stagGridLayoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(stagGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);
        loadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        getLoaderManager();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(ONSAVEINSTANCESTATE_KEY)) {
                ONSAVEINSTANCESTATE_MOVIES = savedInstanceState
                        .getParcelableArrayList(ONSAVEINSTANCESTATE_KEY);
                recyclerView.setVisibility(View.VISIBLE);
                movieAdapter.setMovieData(ONSAVEINSTANCESTATE_MOVIES);
            }
        } else {
            loadMoviesData(SELECTED_OPTION);
        }

    }
    private void loadMoviesData (String sortBy){
        showMoviesDataView();
        new FetchMovieTask().execute(sortBy);
    }
    private void loadFavoriteMovies (){
        showMoviesDataView();
        getLoaderManager().initLoader(TASK_LOADER_ID, null, callbacks);
    }

    public void onClick(MovieInfo movie) {
        Context context = this;
        Class destinationActivity = MovieDetail.class;
        Intent movieDetails = new Intent(context, destinationActivity);
        Bundle extras = new Bundle();

        extras.putString("id",movie.getId());
        extras.putString("title",movie.getTitle());
        extras.putString("description",movie.getDescription());
        extras.putString("poster",movie.getPosterPath());
        extras.putString("release_date",movie.getReleaseDate());
        extras.putString("rate",movie.getRate());

        movieDetails.putExtras(extras);
        startActivity(movieDetails);
    }
    private void showMoviesDataView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        recyclerView.setVisibility(View.INVISIBLE);

        Toast.makeText(this, "An internet connection error. Please try again",
                Toast.LENGTH_LONG).show();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieInfo> doInBackground(String... params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            boolean internetState = (netInfo != null && netInfo.isConnected());

            if (params.length == 0 || !internetState) {
                return null;
            }

            String sortBy = params[0];
            URL moviewRequestUrl = NetworkUtils.buildUrl(sortBy,null,null);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviewRequestUrl);

                ArrayList<MovieInfo> simpleJsonMovieData = MovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, jsonMovieResponse);

                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList movieData) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMoviesDataView();
                ONSAVEINSTANCESTATE_MOVIES = movieData;
                movieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SELECTED_OPTION.equals("favorite"))
        {
            getLoaderManager().restartLoader(TASK_LOADER_ID, null, callbacks);
        }else {
            movieAdapter.setMovieData(ONSAVEINSTANCESTATE_MOVIES);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mi_srtby_most_pop) {
            movieAdapter.setMovieData(null);
            SELECTED_OPTION = "popular";
            loadMoviesData("popular");
            getLoaderManager().destroyLoader(TASK_LOADER_ID);
            return true;
        }else if(id == R.id.mi_srtby_top_rate) {
            movieAdapter.setMovieData(null);
            SELECTED_OPTION = "top rated";
            loadMoviesData("top rated");
            getLoaderManager().destroyLoader(TASK_LOADER_ID);
            return true;
        }else if(id == R.id.mi_srtby_fav_mov) {
            movieAdapter.setMovieData(null);
            SELECTED_OPTION = "favorite";
            loadFavoriteMovies();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new AsyncTaskLoader<Cursor>(MainActivity.this) {

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
                                null,
                                null,
                                null,
                                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_TITLE);

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
            ArrayList<MovieInfo> mArrayList = new ArrayList<MovieInfo>();
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_ID));
                String title = cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_TITLE));
                String posterPath = cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_POSTER_PATH));
                String description = cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_DESC));
                Float voteAverage = cursor.getFloat(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_VOTE_AVG));
                String releaseDate = cursor.getString(cursor.getColumnIndex(DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_REL_DATE));
                mArrayList.add(new MovieInfo(String.valueOf(id),title,posterPath,description,String.valueOf(voteAverage),releaseDate)); //add the item
                cursor.moveToNext();
            }
            recyclerView.setVisibility(View.VISIBLE);
//            ONSAVEINSTANCESTATE_MOVIES = mArrayList;
            movieAdapter.setMovieData(mArrayList);

        }

        @Override
        public void onLoaderReset(Loader loader) {
            movieAdapter.setMovieData(null);
        }
    };

}
