package com.example.moviesapplication.utilities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.moviesapplication.database.MovieDatabase;
import com.example.moviesapplication.entity.Movies;
import com.example.moviesapplication.model.MoviesList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRemoteDataSource {

    private static MovieRemoteDataSource instance;

    private final MovieApiService movieApiService;
    private MovieDatabase movieDatabase;
    private MovieRemoteDataLocal remoteDataLocal;

    public MovieRemoteDataSource(MovieApiService movieApiService, MovieDatabase movieDatabase) {
        this.movieApiService = movieApiService;
        this.movieDatabase = movieDatabase;
    }

    public static MovieRemoteDataSource getInstance(MovieApiService movieApiService, MovieDatabase movieDatabase) {
        if (instance == null) {
            instance = new MovieRemoteDataSource(movieApiService, movieDatabase);
        }
        return instance;
    }

    public void setListener(MovieRemoteDataLocal remoteDataLocal){
        this.remoteDataLocal = remoteDataLocal;
    }

    public void setPopularMoviesData(){
        Call<MoviesList> movieResultCallback = movieApiService.getPopularMovies();
        movieResultCallback.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if(response.isSuccessful()) {
                    List<Movies> movies = response.body().getResults();
                    for(Movies movie: movies){
                        movie.setTag("popular");
                        movieDatabase.movieDao().insertMovie(movie);
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                getAllMovies("popular");
            }
        });
    }

    public void setTopRatedMoviesData() {
        Call<MoviesList> movieResultCallback = movieApiService.getTopRatedMovies();
        movieResultCallback.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.isSuccessful()) {
                    List<Movies> movies = response.body().getResults();
                    for(Movies movie: movies) {
                        movie.setTag("top rated");
                        movieDatabase.movieDao().insertMovie(movie);
                    }
                    remoteDataLocal.setAllMovies();
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                getAllMovies("top rated");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void getAllMovies(String tag){
        new AsyncTask<Void, Void, List<Movies>>(){
            @Override
            protected List<Movies> doInBackground(Void... params){
                return movieDatabase.movieDao().loadMoviesByTag(tag);
            }
            @Override
            protected void onPostExecute(List<Movies> result){
                super.onPostExecute(result);
                remoteDataLocal.setAllMovies(result);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void bookmarkMovie(String doBookmark, int movieId){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieDatabase.movieDao().updateBookmarkedMovie(movieId, doBookmark);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void fetchMovie(String movieId){
        new AsyncTask<Void, Void, Movies>(){
            @Override
            protected Movies doInBackground(Void... params){
                return movieDatabase.movieDao().loadMovieByMovieId(Integer.parseInt(movieId));
            }
            @Override
            protected void onPostExecute(Movies movie){
                super.onPostExecute(movie);
                remoteDataLocal.getMovieDetail(movie);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void getBookmarkedMovies(){
        new AsyncTask<Void, Void, List<Movies>>(){
            @Override
            protected List<Movies> doInBackground(Void... params){
                return movieDatabase.movieDao().loadBookmarkedMovies("yes");
            }
            @Override
            protected void onPostExecute(List<Movies> result){
                super.onPostExecute(result);
                remoteDataLocal.getBookmarkedMovies(result);
            }
        }.execute();
    }

    public void getSearchMovies(String query){
        Call<MoviesList> movieResultCallback = movieApiService.getSearchedMoviesDetail(query);
        movieResultCallback.enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.isSuccessful()) {
                    remoteDataLocal.getSearchedMovies(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                getMovies(query);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void getMovies(String title){
        new AsyncTask<Void, Void, List<Movies>>(){
            @Override
            protected List<Movies> doInBackground(Void... params){
                return movieDatabase.movieDao().loadMovieByTitle(title);
            }
            @Override
            protected void onPostExecute(List<Movies> result){
                super.onPostExecute(result);
                remoteDataLocal.getSearchedMovies(result);
            }
        }.execute();
    }
}
