package com.example.moviesapplication.model;

import com.example.moviesapplication.entity.Movies;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MoviesList implements Serializable {

    public int getPage() {
        return page;
    }

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Movies> results;

    public ArrayList<Movies> getResults() {
        return results;
    }
}
