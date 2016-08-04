package com.android.jimitjaishwal.popularmovies.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jimit Jaishwal on 7/25/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.android.jimitjaishwal.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE_MOVIES = "movies";
    public static final String PATH_MOVIES_TRAILERS = "trailer";
    public static final String PATH_MOVIE_REVIEWS = "reviews";

    public static final class FavouriteMovie implements BaseColumns {

        public static final Uri BASE_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_FAVOURITE_MOVIES)
                .build();

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;

        public static final String CONTENT_DIR_TYPPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE_MOVIES;

        public static final Uri buildMovieWithID(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    }

    public static final class MovieTrailers implements BaseColumns {

        public static final Uri BASE_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES_TRAILERS)
                .build();

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES_TRAILERS;

        public static final String CONTENT_DIR_TYPPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES_TRAILERS;

        public static final Uri buildMovieTrailerWithMovieID(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_TRAILER_KEY = "key";

        public static final String COLUMN_TRAILER_NAME = "name";

    }

    public static final class Reviews implements BaseColumns {

        public static final Uri BASE_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIE_REVIEWS)
                .build();

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;

        public static final String CONTENT_DIR_TYPPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;

        public static final Uri buildMovieReviewsWithMovieID(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_CONTENT = "content";
    }

}
