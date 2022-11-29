package edu.northeastern.nowornever.habit;

import java.util.UUID;

import edu.northeastern.nowornever.utils.Utils;

public class Note {
    private String uuid, noteName, noteDescription, createdDate;

    public Note() {
    }

    public Note(String noteName, String noteDescription) {
        this.noteName = noteName;
        this.noteDescription = noteDescription;
        this.uuid = UUID.randomUUID().toString();
        this.createdDate = Utils.simpleDate();
    }

    public String getUuid() {
        return uuid;
    }

    public String getNoteName() {
        return noteName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
