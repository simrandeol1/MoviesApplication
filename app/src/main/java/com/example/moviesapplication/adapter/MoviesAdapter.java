package com.example.moviesapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapplication.R;
import com.example.moviesapplication.entity.Movies;
import com.example.moviesapplication.ui.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    List<Movies> MovieList ;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w300";

    public MoviesAdapter(Context context)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.MovieList = new ArrayList<>();
    }
    public void setMovieList(List<Movies> movieList)
    {
        this.MovieList=movieList ;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movies_item_lay, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movies movies = MovieList.get(position);

        Glide.with(mContext).load(BASE_URL_IMG+movies.getPosterPath()).placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error).into(holder.imageView);
        holder.itemView.setOnClickListener(view -> {
            Intent myIntent = new Intent(mContext, MovieDetailActivity.class);
            myIntent.putExtra("movie_id", String.valueOf(movies.getMovieId()));
            mContext.startActivity(myIntent);
        });
    }

    @Override
    public int getItemCount() {

        return (MovieList == null) ? 0 : MovieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);

        }
    }
}
