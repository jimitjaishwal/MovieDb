package com.android.jimitjaishwal.popularmovies.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.jimitjaishwal.popularmovies.app.data.MovieContract.FavouriteMovie;
import com.android.jimitjaishwal.popularmovies.app.data.MovieContract.MovieTrailers;

/**
 * Created by Jimit Jaishwal on 7/25/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 3;


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_SQL_FAVOURITE_MOVIE_TABLE =
                "CREATE TABLE " + FavouriteMovie.TABLE_NAME + "( "
                        + FavouriteMovie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + FavouriteMovie.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                        + FavouriteMovie.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                        + FavouriteMovie.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                        + FavouriteMovie.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                        + FavouriteMovie.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                        + FavouriteMovie.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                        + FavouriteMovie.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                        " UNIQUE ( " + FavouriteMovie.COLUMN_MOVIE_ID + ")ON CONFLICT REPLACE );";

        final String CREATE_SQL_MOVIE_TRAILER_TABLE =
                "CREATE TABLE " + MovieTrailers.TABLE_NAME + " ( "
                        + MovieTrailers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MovieTrailers.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                        + MovieTrailers.COLUMN_TRAILER_KEY + " TEXT NOT NULL, "
                        + MovieTrailers.COLUMN_TRAILER_NAME + " TEXT NOT NULL, "

                        + " FOREIGN KEY ( " + MovieTrailers.COLUMN_MOVIE_ID + " ) REFERENCES "
                        + FavouriteMovie.TABLE_NAME + " ( " + FavouriteMovie.COLUMN_MOVIE_ID + "),"
                        + " UNIQUE ( " + MovieTrailers.COLUMN_MOVIE_ID + ")ON CONFLICT REPLACE);";

        final String CREATE_SQL_MOVIE_REVIEW_TABLE =
                " CREATE TABLE " + MovieContract.Reviews.TABLE_NAME + "( "
                        + MovieContract.Reviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MovieContract.Reviews.COLUMN_AUTHOR + " TEXT NOT NULL, "
                        + MovieContract.Reviews.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                        + MovieContract.Reviews.COLUMN_CONTENT + " TEXT NOT NULL, "

                        + " FOREIGN KEY ( " + MovieContract.Reviews.COLUMN_MOVIE_ID + " )REFERENCES "
                        + FavouriteMovie.TABLE_NAME + " ( " + MovieContract.Reviews.COLUMN_MOVIE_ID + "));";

        db.execSQL(CREATE_SQL_FAVOURITE_MOVIE_TABLE);
        db.execSQL(CREATE_SQL_MOVIE_REVIEW_TABLE);
        db.execSQL(CREATE_SQL_MOVIE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXIST " + FavouriteMovie.TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXIST " + MovieTrailers.TABLE_NAME);
        db.execSQL("  DROP TABLE IF EXIST " + MovieContract.Reviews.TABLE_NAME);
    }
}
