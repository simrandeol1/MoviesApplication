package com.example.moviesapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.moviesapplication.adapter.AllMoviesAdapter;
import com.example.moviesapplication.entity.Movies;
import com.example.moviesapplication.ui.BookMarkedMoviesActivity;
import com.example.moviesapplication.ui.SearchResultsActivity;
import com.example.moviesapplication.utilities.MovieRemoteDataLocal;
import com.example.moviesapplication.utilities.MovieRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieRemoteDataLocal {

    private RecyclerView allMoviesRv;
    private AllMoviesAdapter allMoviesAdapter;
    private Context context;
    private List<List<Movies>> allMovieList = new ArrayList<>();
    private SearchView searchView;
    private Toolbar myToolbar;
    private SearchManager searchManager;
    private RelativeLayout errorLay;

    MovieRemoteDataSource movieRemoteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(movieRemoteDataSource == null)
            movieRemoteDataSource = BaseClass.getMovieRemoteDataSource(this);
        setToolbar();
        init();
        setRefreshLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));

        setQueryListener();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_bookmark) {
            Intent myIntent = new Intent(context, BookMarkedMoviesActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (id == R.id.action_home) {
            final Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToolbar() {
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void init(){
        context = this;

        allMoviesRv = findViewById(R.id.all_movies_rv);
        errorLay = findViewById(R.id.error_layout);

        allMoviesRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        allMoviesAdapter = new AllMoviesAdapter(context);
        allMoviesRv.setAdapter(allMoviesAdapter);

        movieRemoteDataSource.setListener(this);
        getData();
    }

    private void setQueryListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(context, SearchResultsActivity.class);
                searchIntent.setAction(Intent.ACTION_SEARCH);
                searchIntent.putExtra(SearchManager.QUERY, query);
                startActivity(searchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    public void changeToolbarText(int toolbarText){
        myToolbar.setTitle(getResources().getString(toolbarText));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(searchView.hasFocus()) {
            searchView.clearFocus();
        }
    }

    private void isNoData(){
        if(allMovieList == null || allMovieList.size() == 0){
            errorLay.setVisibility(View.VISIBLE);
        }
        else{
            errorLay.setVisibility(View.GONE);
        }
    }

    private void setData(){
        allMoviesAdapter.setMovieList(allMovieList);
        allMoviesAdapter.notifyDataSetChanged();
        isNoData();
    }

    private void getData(){
        if(BaseClass.isNetworkAvailable(this)) {
            movieRemoteDataSource.setPopularMoviesData();
            movieRemoteDataSource.setTopRatedMoviesData();
        }else {
            movieRemoteDataSource.getAllMovies("popular");
            movieRemoteDataSource.getAllMovies("top rated");
        }
    }

    @Override
    public void setAllMovies(List<Movies> allMovies) {
        if(allMovieList != null && allMovieList.size() == 2)
            allMovieList.clear();
        allMovieList.add(allMovies);
        setData();
    }

    @Override
    public void setAllMovies() {
        movieRemoteDataSource.getAllMovies("popular");
        movieRemoteDataSource.getAllMovies("top rated");
    }

    @Override
    public void getMovieDetail(Movies movie) {

    }

    @Override
    public void getBookmarkedMovies(List<Movies> bookmarkedMovies) {

    }

    @Override
    public void getSearchedMovies(List<Movies> searchedMovies) {

    }

    public void setRefreshLayout(){
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}