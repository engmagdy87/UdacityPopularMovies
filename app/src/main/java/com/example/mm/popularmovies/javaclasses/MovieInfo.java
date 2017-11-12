package com.example.mm.popularmovies.javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MM on 10/9/2017.
 */

public class MovieInfo implements Parcelable {
    private String id;
    private String title;
    private String posterPath;
    private String description;
    private String voteAverage;
    private String releaseDate;
public MovieInfo(String id, String title,String poster_path,String description,String vote_average,String release_date){
    this.id = id;
    this.title = title;
    this.posterPath = poster_path;
    this.description = description;
    this.voteAverage = vote_average;
    this.releaseDate = release_date;
}

    protected MovieInfo(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        description = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(description);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    public String getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getPosterPath() {
        return this.posterPath;
    }
    public String getDescription() {
        return this.description;
    }
    public String getRate() {
        return this.voteAverage;
    }
    public String getReleaseDate() {
        return this.releaseDate;
    }
}
