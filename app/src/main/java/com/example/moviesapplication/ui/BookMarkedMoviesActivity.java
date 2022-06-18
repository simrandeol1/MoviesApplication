package com.example.moviesapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.moviesapplication.utilities.MovieRemoteDataLocal;
import com.example.moviesapplication.utilities.MovieRemoteDataSource;

import java.util.List;

public class BookMarkedMoviesActivity extends MainActivity implements MovieRemoteDataLocal {

    private Context context;
    private RecyclerView bookmarkedMoviesRv;
    private MoviesAdapter bookmarkedAdapter;
    private String TAG = "simsim";
    private List<Movies> bookmarkedList;
    private RelativeLayout errorLay;
    private MovieRemoteDataSource movieRemoteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_activity);
        if(movieRemoteDataSource == null)
            movieRemoteDataSource = BaseClass.getMovieRemoteDataSource(this);
        init();
        setToolbar();
        changeToolbarText(R.string.bookmarked_movies);
    }

    private void init() {
        context = this;

        errorLay = findViewById(R.id.error_layout);

        bookmarkedMoviesRv = findViewById(R.id.bookmarked_movies_rv);
        bookmarkedMoviesRv.setLayoutManager(new GridLayoutManager(context, 2));
        bookmarkedAdapter = new MoviesAdapter(context);
        movieRemoteDataSource.getBookmarkedMovies();
        movieRemoteDataSource.setListener(this);
        bookmarkedMoviesRv.setAdapter(bookmarkedAdapter);

    }

    private void isNoData(){
        if(bookmarkedList == null || bookmarkedList.size() == 0)
            errorLay.setVisibility(View.VISIBLE);
        else
            errorLay.setVisibility(View.GONE);
    }

    @Override
    public void setAllMovies(List<Movies> allMovies) {
    }

    @Override
    public void getMovieDetail(Movies movie) {
    }

    @Override
    public void getBookmarkedMovies(List<Movies> bookmarkedMovies) {
        if(bookmarkedList != null)
            bookmarkedList.clear();

        bookmarkedList = bookmarkedMovies;
        isNoData();
        bookmarkedAdapter.setMovieList(bookmarkedMovies);
        bookmarkedAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSearchedMovies(List<Movies> searchedMovies) {

    }
}
