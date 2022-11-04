package edu.northeastern.nowornever.model.sticker;

import java.util.List;

public class User {

    public String username;
    public List<Sticker> receivedStickers;


    public User() {
    }

    public User(String username, List<Sticker> receivedStickers) {
        this.username = username;
        this.receivedStickers = receivedStickers;
    }

}
