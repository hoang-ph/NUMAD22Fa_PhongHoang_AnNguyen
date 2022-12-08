package edu.northeastern.nowornever.habit;

import java.util.UUID;

import edu.northeastern.nowornever.utils.Utils;

public class Note {
    private String uuid, noteName, noteDescription, createdDate, username, habitUuid;

    public Note() {
    }

    public Note(String noteName, String noteDescription, String username, String habitUuid) {
        this.noteName = noteName;
        this.noteDescription = noteDescription;
        this.username = username;
        this.habitUuid = habitUuid;
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

    public String getUsername() {
        return username;
    }

    public String getHabitUuid() {
        return habitUuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
