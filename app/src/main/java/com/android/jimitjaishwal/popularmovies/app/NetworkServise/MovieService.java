package com.android.jimitjaishwal.popularmovies.app.NetworkServise;

import com.android.jimitjaishwal.popularmovies.app.Models.MovieModel;
import com.android.jimitjaishwal.popularmovies.app.Models.TrailerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jimit Jaishwal on 7/26/2016.
 */
public interface MovieService {

    @GET("discover/movie?api_key=149c3ccaa00eeb61331e4da7734fd1af")
    Call<MovieModel.MovieResponse> getPopularMovies(@Query("sort_by") String sort_by);

    @GET("movie/{id}/videos?api_key=149c3ccaa00eeb61331e4da7734fd1af")
    Call<TrailerModel.TrailerResponse> getTrailerResponse(@Path("id") int id);
}
