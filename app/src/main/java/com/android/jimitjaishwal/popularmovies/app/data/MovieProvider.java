package com.android.jimitjaishwal.popularmovies.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Jimit Jaishwal on 7/25/2016.
 */
public class MovieProvider extends ContentProvider {

    private MovieDbHelper mOpenHelper;
    private UriMatcher matcher = buildUriMatcher();
    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;
    private static final int TRAILERS = 200;
    private static final int TRAILERS_WITH_ID = 201;
    private static final int REVIEWS = 300;
    private static final int REVIEWS_WITH_ID = 301;

    private UriMatcher buildUriMatcher() {
        String AUTHORITY = MovieContract.CONTENT_AUTHORITY;
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES, MOVIES);
        matcher.addURI(AUTHORITY, MovieContract.PATH_FAVOURITE_MOVIES + "/#", MOVIES_WITH_ID);
        matcher.addURI(AUTHORITY, MovieContract.PATH_MOVIES_TRAILERS, TRAILERS);
        matcher.addURI(AUTHORITY, MovieContract.PATH_MOVIES_TRAILERS + "/#", TRAILERS_WITH_ID);
        matcher.addURI(AUTHORITY, MovieContract.PATH_MOVIE_REVIEWS, REVIEWS);
        matcher.addURI(AUTHORITY, MovieContract.PATH_MOVIE_REVIEWS + "/#", REVIEWS_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    public static final String movieWithMovieIdSelection =
            MovieContract.FavouriteMovie.TABLE_NAME + "." +
                    MovieContract.FavouriteMovie.COLUMN_MOVIE_ID + "=?";


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg, String sortOrder) {

        Cursor reCursor;
        switch (matcher.match(uri)) {

            case MOVIES: {

                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.FavouriteMovie.TABLE_NAME,
                                projection,
                                selection,
                                selectionArg,
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }

            case MOVIES_WITH_ID: {

                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.FavouriteMovie.TABLE_NAME,
                                projection,
                                MovieContract.FavouriteMovie.COLUMN_MOVIE_ID + "=?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))},
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }

            case TRAILERS: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieTrailers.TABLE_NAME,
                                projection,
                                selection,
                                selectionArg,
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }

            case TRAILERS_WITH_ID: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieTrailers.TABLE_NAME,
                                projection,
                                MovieContract.MovieTrailers.COLUMN_MOVIE_ID + "=?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))},
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }

            case REVIEWS: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.Reviews.TABLE_NAME,
                                projection,
                                selection,
                                selectionArg,
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }

            case REVIEWS_WITH_ID: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.Reviews.TABLE_NAME,
                                projection,
                                MovieContract.Reviews.COLUMN_MOVIE_ID + "=?",
                                new String[]{String.valueOf(ContentUris.parseId(uri))},
                                null,
                                null,
                                sortOrder);
                return reCursor;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
            }
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = matcher.match(uri);

        switch (match) {

            case MOVIES: {
                return MovieContract.FavouriteMovie.CONTENT_DIR_TYPPE;
            }

            case MOVIES_WITH_ID: {
                return MovieContract.FavouriteMovie.CONTENT_ITEM_TYPE;
            }

            case TRAILERS: {
                return MovieContract.MovieTrailers.CONTENT_DIR_TYPPE;
            }

            case TRAILERS_WITH_ID: {
                return MovieContract.MovieTrailers.CONTENT_ITEM_TYPE;
            }

            case REVIEWS: {
                return MovieContract.Reviews.CONTENT_DIR_TYPPE;
            }

            case REVIEWS_WITH_ID: {
                return MovieContract.Reviews.CONTENT_ITEM_TYPE;
            }

            default: {
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri insertedUri;
        long id;

        switch (matcher.match(uri)) {

            case MOVIES: {
                id = db.insert(MovieContract.FavouriteMovie.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    insertedUri = MovieContract.FavouriteMovie.buildMovieWithID(id);
                } else {
                    throw new android.database.SQLException("Failed to inserted Rows : " + uri);
                }
                break;
            }

            case TRAILERS: {
                id = db.insert(MovieContract.MovieTrailers.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    insertedUri = MovieContract.MovieTrailers.buildMovieTrailerWithMovieID(id);
                } else {
                    throw new android.database.SQLException("Failed to inserted Rows : " + uri);
                }
                break;
            }

            case REVIEWS: {
                id = db.insert(MovieContract.Reviews.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    insertedUri = MovieContract.Reviews.buildMovieReviewsWithMovieID(id);
                } else {
                    throw new android.database.SQLException("Failed to inserted Rows : " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
            }
        }

        if (id > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return insertedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deletedRows;
        int match = matcher.match(uri);

        if (null == selection) {
            selection = "1";
        }

        switch (match) {

            case MOVIES: {
                deletedRows = db.delete(MovieContract.FavouriteMovie.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case TRAILERS: {
                deletedRows = db.delete(MovieContract.MovieTrailers.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case REVIEWS: {
                deletedRows = db.delete(MovieContract.Reviews.TABLE_NAME, selection, selectionArgs);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
            }
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = matcher.match(uri);
        int updatedRows;

        switch (match) {

            case MOVIES: {
                updatedRows = db.update(MovieContract.FavouriteMovie.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }

            case TRAILERS: {
                updatedRows = db.update(MovieContract.MovieTrailers.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }

            case REVIEWS: {
                updatedRows = db.update(MovieContract.Reviews.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
            }
        }
        if (updatedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = matcher.match(uri);

        switch (match) {

            case MOVIES: {
                db.beginTransaction();
                int insertedRows = 0;

                try {
                    for (ContentValues value : values) {
                        long id = -1;
                        try {
                            id = db.insert(MovieContract.FavouriteMovie.TABLE_NAME, null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.e("ContentProvider ", "bulkInsert: "
                                    + value.getAsString(MovieContract.FavouriteMovie.COLUMN_MOVIE_ID)
                                    + "but values in already in database");
                        }
                        if (id != -1) {
                            insertedRows++;
                        }
                    }
                    if (insertedRows > 0) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
                if (insertedRows > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return insertedRows;
            }
            default:
                return super.bulkInsert(uri, values);
        }


    }
}
