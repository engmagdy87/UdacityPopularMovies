package com.example.mm.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mm.popularmovies.javaclasses.MovieTrailers;
import com.example.mm.popularmovies.R;

import java.util.ArrayList;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersAdapterViewHolder> {

    private ArrayList<MovieTrailers> movieTrailer;

    private final MovieTrailersAdapter.MovieTrailerAdapterOnClickHandler clickHandler;

    public interface MovieTrailerAdapterOnClickHandler {
        void onClick(MovieTrailers movie);
    }

    public MovieTrailersAdapter(MovieTrailerAdapterOnClickHandler mClickHandler){
        clickHandler = mClickHandler;
    }

    public class MovieTrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView trailerTitle;

        public MovieTrailersAdapterViewHolder(View view){
            super(view);
            trailerTitle = (TextView) view.findViewById(R.id.tv_trailer);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieTrailers movie = movieTrailer.get(adapterPosition);
            clickHandler.onClick(movie);
        }
    }

    @Override
    public MovieTrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int LayoutIdForMovieInList = R.layout.movie_trailers;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(LayoutIdForMovieInList, viewGroup,false);
        return new MovieTrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailersAdapterViewHolder holder, int position) {
        holder.trailerTitle.setText(movieTrailer.get(position).getName());
    }



    @Override
    public int getItemCount() {
        if(movieTrailer == null) return 0;
        return movieTrailer.size();
    }
    public void setMovieTrailer(ArrayList mMovieTrailer) {
        movieTrailer = mMovieTrailer;
        notifyDataSetChanged();
    }

}
