package com.example.mm.popularmovies.javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MM on 10/29/2017.
 */

public class MovieReviews implements Parcelable {
    private String author;
    private String content;
    public MovieReviews(String author, String content){
        this.author = author;
        this.content = content;
    }

    protected MovieReviews(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };

    public String getAuthor() {
        return this.author;
    }
    public String getContent() {
        return this.content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }
}
