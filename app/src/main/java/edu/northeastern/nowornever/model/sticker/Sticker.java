package edu.northeastern.nowornever.model.sticker;

import edu.northeastern.nowornever.utils.Utils;

public class Sticker {

    public String type;
    public String senderUsername;
    public String dateSent;

    public Sticker() {
    }

    public Sticker(String type, String senderUsername) {
        this.type = type;
        this.senderUsername = senderUsername;
        this.dateSent = Utils.date();
    }

    public String getStickerInfo() {
        return "Sticker was sent by " + this.senderUsername + " at " + this.dateSent;
    }
}
