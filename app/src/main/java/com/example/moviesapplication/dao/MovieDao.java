package com.example.moviesapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.moviesapplication.entity.Movies;

import java.util.List;

@Dao
    public interface MovieDao {

        @Query("SELECT * FROM movie")
        List<Movies> loadAllMovies();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertMovie(Movies Movie);

        @Delete
        void deleteMovie(Movies movie);

        @Query("SELECT DISTINCT * FROM movie WHERE bookmarked = :isBookmarked")
        List<Movies> loadBookmarkedMovies(String isBookmarked);

        @Query("UPDATE movie SET bookmarked= :isBookmarked WHERE _id = :id")
        int updateBookmarkedMovie(int id, String isBookmarked);

        @Query("SELECT * FROM movie WHERE movie_id = :movieId")
        Movies loadMovieByMovieId(int movieId);

        @Query("SELECT DISTINCT * FROM movie WHERE title LIKE :title OR original_title LIKE :title")
        List<Movies> loadMovieByTitle(String title);

        @Query("SELECT * FROM movie WHERE tag = :tag")
        List<Movies> loadMoviesByTag(String tag);

}

