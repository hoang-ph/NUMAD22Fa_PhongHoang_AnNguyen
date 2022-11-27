package edu.northeastern.nowornever.habit;

public class HabitImage {

    private final String imgID, imgUri;

    public HabitImage(String imgID, String uri) {
        this.imgID = imgID;
        this.imgUri = uri;
    }

    public String getImgID() {
        return imgID;
    }

    public String getImgUri() {
        return imgUri;
    }
}
