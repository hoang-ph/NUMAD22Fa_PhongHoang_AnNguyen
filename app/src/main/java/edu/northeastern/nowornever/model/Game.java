package edu.northeastern.nowornever.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Game implements Parcelable {
    private int id;
    private String title;
    @SerializedName("short_description")
    private String shortDescription;
    private String genre;
    private String platform;
    private String publisher;
    private String developer;
    @SerializedName("release_date")
    private String releaseDate;

    public Game(int id, String title, String shortDescription, String genre, String platform, String publisher, String developer, String releaseDate) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.genre = genre;
        this.platform = platform;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    protected Game(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.shortDescription = in.readString();
        this.genre = in.readString();
        this.platform = in.readString();
        this.publisher = in.readString();
        this.developer = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(shortDescription);
        parcel.writeString(genre);
        parcel.writeString(platform);
        parcel.writeString(publisher);
        parcel.writeString(developer);
        parcel.writeString(releaseDate);
    }
}
