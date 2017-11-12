package com.example.mm.popularmovies.javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MM on 10/27/2017.
 */

public class MovieTrailers implements Parcelable {
    private String key;
    private String name;
    public MovieTrailers(String key, String name){
        this.key = key;
        this.name = name;
    }

    protected MovieTrailers(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<MovieTrailers> CREATOR = new Creator<MovieTrailers>() {
        @Override
        public MovieTrailers createFromParcel(Parcel in) {
            return new MovieTrailers(in);
        }

        @Override
        public MovieTrailers[] newArray(int size) {
            return new MovieTrailers[size];
        }
    };

    public String getId() {
        return this.key;
    }
    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(name);
      }

}
