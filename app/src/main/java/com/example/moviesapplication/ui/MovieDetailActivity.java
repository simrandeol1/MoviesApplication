package com.example.moviesapplication.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviesapplication.BaseClass;
import com.example.moviesapplication.MainActivity;
import com.example.moviesapplication.R;
import com.example.moviesapplication.database.MovieDatabase;
import com.example.moviesapplication.entity.Movies;
import com.example.moviesapplication.model.MoviesList;
import com.example.moviesapplication.utilities.FetchData;
import com.example.moviesapplication.utilities.MovieApiService;
import com.example.moviesapplication.utilities.MovieRemoteDataLocal;
import com.example.moviesapplication.utilities.MovieRemoteDataSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends MainActivity implements MovieRemoteDataLocal {

    private TextView titleTv, taglineTv, voteTv, releaseDateTv, overViewTv, bookmarkTv;
    private ImageView movieImg, bookmarkIv;
    private String movieId;
    private Movies movie;
    private String TAG = "simsim";
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w300";
    private Context context;
    private LinearLayout bookmarkLay;
    private boolean bookmarked;
    private RelativeLayout errorLay;
    private MovieRemoteDataSource movieRemoteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_lay);
        if(movieRemoteDataSource == null)
            movieRemoteDataSource = BaseClass.getMovieRemoteDataSource(this);
        init();
        setToolbar();
        changeToolbarText(R.string.about_movies);

    }

    private void init(){
        context = this;

        movieImg  = findViewById(R.id.movie_image);
        titleTv  = findViewById(R.id.title_tv);
        taglineTv = findViewById(R.id.tagline_tv);
        voteTv = findViewById(R.id.vote_tv);
        releaseDateTv = findViewById(R.id.date_tv);
        overViewTv = findViewById(R.id.overview_tv);
        bookmarkTv = findViewById(R.id.bookmark_tv);
        bookmarkIv = findViewById(R.id.bookmark_iv);
        bookmarkLay = findViewById(R.id.bookmark_lay);
        errorLay = findViewById(R.id.error_layout);

        movie = new Movies();
        Intent myIntent = getIntent();
        movieId = myIntent.getStringExtra("movie_id");

        movieRemoteDataSource.setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        movieRemoteDataSource.fetchMovie(movieId);
    }
    private void setData(){
        if(movie == null){
            errorLay.setVisibility(View.VISIBLE);
            return;
        }
        errorLay.setVisibility(View.GONE);
        Glide.with(this).load(BASE_URL_IMG+movie.getPosterPath()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error).into(movieImg);
        titleTv.setText(movie.getTitle());
        taglineTv.setText(movie.getTagline());
        overViewTv.setText(movie.getOverview());
        releaseDateTv.setText(getResources().getString(R.string.release_date)+movie.getReleaseDate());
        voteTv.setText(getResources().getString(R.string.rating)+ movie.getVoteAverage());
        bookmarkIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));

        getIsBookmarked(movie.getBookmarked());

        bookmarkLay.setOnClickListener(v->{
            if(!bookmarked){
                movieRemoteDataSource.bookmarkMovie("no", movie.get_id());
                movie.setBookmarked("no");
                bookmarkIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                bookmarkTv.setText(getResources().getString(R.string.bookmark));
            }else {
                movie.setBookmarked("yes");
                movieRemoteDataSource.bookmarkMovie("yes", movie.get_id());
                bookmarkIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmarked));
                bookmarkTv.setText(getResources().getString(R.string.bookmarked));
            }
            bookmarked = !bookmarked;
        });
    }

    private void getIsBookmarked(String isBookMarked){
        if("yes".equals(isBookMarked)){
            movie.setBookmarked("yes");
            movieRemoteDataSource.bookmarkMovie("yes", movie.get_id());
            bookmarkIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmarked));
            bookmarkTv.setText(getResources().getString(R.string.bookmarked));
        }else{
            movie.setBookmarked("no");
            movieRemoteDataSource.bookmarkMovie("no", movie.get_id());
            bookmarkIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
            bookmarkTv.setText(getResources().getString(R.string.bookmark));
        }
    }

    @Override
    public void setAllMovies(List<Movies> allMovies) {
    }

    @Override
    public void getMovieDetail(Movies movie) {
        this.movie = movie;
        setData();
    }

    @Override
    public void getBookmarkedMovies(List<Movies> bookmarkedMovies) {

    }

    @Override
    public void getSearchedMovies(List<Movies> searchedMovies) {

    }

}
