package com.example.moviesapplication.utilities;

import com.example.moviesapplication.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MovieApiService {
        @Headers("api-key: " + "c3f84d14f7f7bbfa82ae5bac50670fb8")
        @GET("movie/popular")
        Call<MoviesList> getPopularMovies();

        @Headers("api-key: " + "c3f84d14f7f7bbfa82ae5bac50670fb8")
        @GET("movie/now_playing")
        Call<MoviesList> getTopRatedMovies();

//        @Headers("api-key: " + "c3f84d14f7f7bbfa82ae5bac50670fb8")
//        @GET("movie/{movie_id}")
//        Call<Movies> getMoviesDetail(@Path("movie_id") String movieId);

        @Headers("api-key: " + "c3f84d14f7f7bbfa82ae5bac50670fb8")
        @GET("search/movie")
        Call<MoviesList> getSearchedMoviesDetail(@Query("query") String query);
}
