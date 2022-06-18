package com.example.moviesapplication.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.moviesapplication.dao.MovieDao;
import com.example.moviesapplication.entity.Movies;

@Database(entities = {Movies.class}, version = 1, exportSchema = false)
    public abstract class MovieDatabase extends RoomDatabase {

        private static final String TAG = MovieDatabase.class.getSimpleName();

        // For Singleton instantiation
        private static final Object LOCK = new Object();
        private static MovieDatabase sInstance;

        public static MovieDatabase getInstance(Context context) {
            if (sInstance == null) {
                synchronized (LOCK) {
                    Log.d(TAG, "Creating new database instance");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "Movies-Database").allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
            Log.d(TAG, "Getting the database instance");
            return sInstance;
        }

        // The associated DAOs for the database
        public abstract MovieDao movieDao();
}
