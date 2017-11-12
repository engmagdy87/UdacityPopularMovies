package com.example.mm.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mm.popularmovies.javaclasses.MovieReviews;
import com.example.mm.popularmovies.R;

import java.util.ArrayList;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewsAdapterViewHolder> {

    private ArrayList<MovieReviews> movieReview;

    private final MovieReviewsAdapter.MovieReviewAdapterOnClickHandler clickHandler;

    public interface MovieReviewAdapterOnClickHandler {
        void onClick(MovieReviews movie);
    }

    public MovieReviewsAdapter(MovieReviewsAdapter.MovieReviewAdapterOnClickHandler mClickHandler){
        clickHandler = mClickHandler;
    }

    public class MovieReviewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView reviewAuthor;
        public final TextView reviewContent;

        public MovieReviewsAdapterViewHolder(View view){
            super(view);
            reviewAuthor = (TextView) view.findViewById(R.id.tv_author);
            reviewContent = (TextView) view.findViewById(R.id.tv_review);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieReviews movie = movieReview.get(adapterPosition);
            clickHandler.onClick(movie);
        }
    }

    @Override
    public MovieReviewsAdapter.MovieReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int LayoutIdForMovieInList = R.layout.movie_reviews;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(LayoutIdForMovieInList, viewGroup,false);
        return new MovieReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsAdapterViewHolder holder, int position) {
        holder.reviewAuthor.setText(movieReview.get(position).getAuthor());
        holder.reviewContent.setText(movieReview.get(position).getContent());
    }
    
    @Override
    public int getItemCount() {
        if(movieReview == null) return 0;
        return movieReview.size();
    }
    public void setMovieReview(ArrayList mMovieReview) {
        movieReview = mMovieReview;
        notifyDataSetChanged();
    }

}

