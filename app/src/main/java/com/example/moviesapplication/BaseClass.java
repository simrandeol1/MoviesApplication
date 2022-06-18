package com.example.moviesapplication;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.moviesapplication.database.MovieDatabase;
import com.example.moviesapplication.utilities.FetchData;
import com.example.moviesapplication.utilities.MovieApiService;
import com.example.moviesapplication.utilities.MovieRemoteDataLocal;
import com.example.moviesapplication.utilities.MovieRemoteDataSource;

public class BaseClass {

    private static MovieRemoteDataSource movieRemoteDataSource;
    private static MovieApiService movieApiService;
    private static MovieDatabase movieDatabase;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private static void makeHelpers(Context context){
        movieApiService = FetchData.getClient().create(MovieApiService.class);
        movieDatabase = MovieDatabase.getInstance(context);
    }

    public static MovieRemoteDataSource getMovieRemoteDataSource(Context context){

        if (movieRemoteDataSource == null) {
            makeHelpers(context);
            movieRemoteDataSource = MovieRemoteDataSource.getInstance(movieApiService, movieDatabase);
        }
        return movieRemoteDataSource;
    }
}
