package com.example.mm.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MM on 10/30/2017.
 */

public class DbContract {
    public static final String AUTHORITY = "com.example.mm.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "favorite_movies";

    public static final class FavoriteMoviesTable implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MDB_MOVIE_ID = "mdb_movie_id";
        public static final String COLUMN_MDB_MOVIE_TITLE = "mdb_movie_title";
        public static final String COLUMN_MDB_MOVIE_POSTER_PATH = "mdb_movie_poster_path";
        public static final String COLUMN_MDB_MOVIE_DESC = "mdb_movie_desc";
        public static final String COLUMN_MDB_MOVIE_VOTE_AVG = "mdb_movie_vote_avg";
        public static final String COLUMN_MDB_MOVIE_REL_DATE = "mdb_movie_rel_date";

    }
}
