package com.example.moviesapplication.utilities;

import com.example.moviesapplication.entity.Movies;

import java.util.List;

public interface MovieRemoteDataLocal {

    void setAllMovies(List<Movies> allMovies);

    void setAllMovies();

    void getMovieDetail(Movies movie);

    void getBookmarkedMovies(List<Movies> bookmarkedMovies);

    void getSearchedMovies(List<Movies> searchedMovies);

}
