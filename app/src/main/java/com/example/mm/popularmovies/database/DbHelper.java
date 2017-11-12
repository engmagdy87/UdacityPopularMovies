package com.example.mm.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by MM on 10/30/2017.
 */

public class DbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "FavMovDb.db";

    private static final int VERSION = 3;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + DbContract.FavoriteMoviesTable.TABLE_NAME + " (" +
                DbContract.FavoriteMoviesTable._ID                + " INTEGER PRIMARY KEY, " +
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_ID + " INTEGER NOT NULL,"+
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_TITLE + " TEXT NOT NULL,"+
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_POSTER_PATH + " TEXT NOT NULL,"+
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_DESC + " TEXT NOT NULL,"+
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_VOTE_AVG + " FLOAT NOT NULL,"+
                DbContract.FavoriteMoviesTable.COLUMN_MDB_MOVIE_REL_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.FavoriteMoviesTable.TABLE_NAME);
        onCreate(db);
    }
}
