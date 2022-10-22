package edu.northeastern.nowornever.model;

import com.google.gson.annotations.SerializedName;

public class Game {
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
}