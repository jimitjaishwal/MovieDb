package com.android.jimitjaishwal.popularmovies.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Jimit Jaishwal on 7/31/2016.
 */
public class Utility {

    public static String getMovies_sort_by(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(
                mContext.getString(R.string.pref_key_movies_sort_by_movieList),
                mContext.getString(R.string.pref_movie_sort_by_popularMovies_desc));
    }
}
