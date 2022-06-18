package com.example.moviesapplication.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapplication.BaseClass;
import com.example.moviesapplication.MainActivity;
import com.example.moviesapplication.R;
import com.example.moviesapplication.adapter.MoviesAdapter;
import com.example.moviesapplication.database.MovieDatabase;
import com.example.moviesapplication.entity.Movies;
import com.example.moviesapplication.model.MoviesList;
import com.example.moviesapplication.utilities.FetchData;
import com.example.moviesapplication.utilities.MovieApiService;
import com.example.moviesapplication.utilities.MovieRemoteDataLocal;
import com.example.moviesapplication.utilities.MovieRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends MainActivity implements MovieRemoteDataLocal {

    private Context context;
    private RecyclerView searchedMoviesRv;
    private MoviesAdapter searchedAdapter;
    private String TAG = "simsim";
    private List<Movies> searchList;
    private RelativeLayout errorLay;
    private String query;
    private MovieRemoteDataSource movieRemoteDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_activity);
        movieRemoteDataSource = BaseClass.getMovieRemoteDataSource(this);
        init();
        setToolbar();
        changeToolbarText(R.string.search_results);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if(query == null)
                return;
            if(!BaseClass.isNetworkAvailable(this))
                movieRemoteDataSource.getMovies(query);
            else
                movieRemoteDataSource.getSearchMovies(query);
        }
    }

    private void init() {
        context = this;

        errorLay = findViewById(R.id.error_layout);
        searchedMoviesRv = findViewById(R.id.bookmarked_movies_rv);
        searchedMoviesRv.setLayoutManager(new GridLayoutManager(context, 2));
        searchedAdapter = new MoviesAdapter(context);
        searchedMoviesRv.setAdapter(searchedAdapter);
        movieRemoteDataSource.setListener(this);
    }

    private void setData(){
        if(searchList == null || searchList.size() == 0)
            errorLay.setVisibility(View.VISIBLE);
        else {
            errorLay.setVisibility(View.GONE);
            searchedAdapter.setMovieList(searchList);
            searchedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setAllMovies(List<Movies> allMovies) {
    }

    @Override
    public void getMovieDetail(Movies movie) {
    }


    @Override
    public void getBookmarkedMovies(List<Movies> bookmarkedMovies) {

    }

    @Override
    public void getSearchedMovies(List<Movies> searchedMovies) {
        searchList = searchedMovies;
        setData();
    }
}
