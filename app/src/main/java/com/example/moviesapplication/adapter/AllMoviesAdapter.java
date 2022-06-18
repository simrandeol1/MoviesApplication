package com.example.moviesapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapplication.R;
import com.example.moviesapplication.entity.Movies;

import java.util.ArrayList;
import java.util.List;

public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.MovieViewHolder> {

    List<List<Movies>> allMovieList ;
    private LayoutInflater mInflater;
    private Context mContext;

    public AllMoviesAdapter(Context context)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.allMovieList = new ArrayList<>();
    }
    public void setMovieList(List<List<Movies>> allMovieList)
    {
        this.allMovieList=allMovieList ;
    }
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movies_row_lay, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        if(position == 0)
            holder.headerTv.setText(mContext.getResources().getString(R.string.popular_movie));
        else if(position == 1)
            holder.headerTv.setText(mContext.getResources().getString(R.string.now_playing_movie));

        holder.moviesRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        MoviesAdapter moviesAdapter = new MoviesAdapter(mContext);
        moviesAdapter.setMovieList(allMovieList.get(position));
        holder.moviesRv.setAdapter(moviesAdapter);
    }

    @Override
    public int getItemCount() {
        return allMovieList.size();
    }
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView moviesRv;
        public TextView headerTv;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviesRv = itemView.findViewById(R.id.movies_rv);
            headerTv = itemView.findViewById(R.id.header_tv);

        }
    }
}
