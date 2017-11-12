package com.example.mm.popularmovies.adapters;
import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mm.popularmovies.javaclasses.MovieInfo;
import com.example.mm.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MovieItemAdapterViewHolder>{
    private ArrayList<MovieInfo> moviesData;
    private Context context;

    private final MovieItemAdapterOnClickHandler clickHandler;

    public interface MovieItemAdapterOnClickHandler {
        void onClick(MovieInfo movie);
    }

    public MovieItemAdapter(Context context,MovieItemAdapterOnClickHandler mClickHandler){
        this.context = context;
        clickHandler = mClickHandler;
    }

    public class MovieItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView moviePoster;

        public MovieItemAdapterViewHolder(View view){
            super(view);
            moviePoster = (ImageView) view.findViewById(R.id.movie_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieInfo movie = moviesData.get(adapterPosition);
            clickHandler.onClick(movie);
        }
    }

    @Override
    public MovieItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int LayoutIdForMovieInList = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(LayoutIdForMovieInList, viewGroup,false);
        return new MovieItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieItemAdapterViewHolder holder, int position) {
        Picasso.with(context).load(moviesData.get(position).getPosterPath()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if(moviesData == null) return 0;
        return moviesData.size();
    }
    public void setMovieData(ArrayList<MovieInfo> movieData) {
        moviesData = movieData;
        notifyDataSetChanged();
    }

}
