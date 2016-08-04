package com.android.jimitjaishwal.popularmovies.app.MovieDbService;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.jimitjaishwal.popularmovies.app.Models.MovieModel;
import com.android.jimitjaishwal.popularmovies.app.NetworkServise.MovieService;
import com.android.jimitjaishwal.popularmovies.app.data.MovieContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jimit Jaishwal on 7/31/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {
    private String LOG_TAG = MovieContract.FavouriteMovie.class.getSimpleName();
    private static final String base_url = "http://api.themoviedb.org/3/";
    private ArrayList<MovieModel> results;
    private Context mContext;

    public FetchMovieTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MovieService service = retrofit.create(MovieService.class);
            Call<MovieModel.MovieResponse> call = service.getPopularMovies(params[0]);
            Response<MovieModel.MovieResponse> response = call.execute();
            MovieModel.MovieResponse movieResponse = response.body();
            results = movieResponse.getResults();

            for (MovieModel movies : results) {

                int movieId = movies.getId();
                String posterPath = movies.getPoster_path();
                String overView = movies.getOverview();
                String releaseDate = movies.getRelease_date();
                String originalTitle = movies.getOriginal_title();
                String backDropPath = movies.getBackdrop_path();
                double voteAverage = movies.getVote_average();

                Vector<ContentValues> cVv = new Vector<ContentValues>();
                ContentValues values = new ContentValues();
                values.put(MovieContract.FavouriteMovie.COLUMN_MOVIE_ID, movieId);
                values.put(MovieContract.FavouriteMovie.COLUMN_POSTER_PATH, posterPath);
                values.put(MovieContract.FavouriteMovie.COLUMN_OVERVIEW, overView);
                values.put(MovieContract.FavouriteMovie.COLUMN_RELEASE_DATE, releaseDate);
                values.put(MovieContract.FavouriteMovie.COLUMN_ORIGINAL_TITLE, originalTitle);
                values.put(MovieContract.FavouriteMovie.COLUMN_BACKDROP_PATH, backDropPath);
                values.put(MovieContract.FavouriteMovie.COLUMN_VOTE_AVERAGE, voteAverage);
                cVv.add(values);

                int insertedRow = 0;
                if (cVv.size() > 0) {
                    ContentValues[] movieTableValue = new ContentValues[cVv.size()];
                    cVv.toArray(movieTableValue);
                    insertedRow = mContext
                            .getContentResolver()
                            .bulkInsert(MovieContract.FavouriteMovie.BASE_URI, movieTableValue);
                }
                Log.d(LOG_TAG, "Movie Table Rows " + insertedRow + " Rows inserted");
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error: " + e);
        }
        return null;
    }
}