package edu.northeastern.nowornever.habit;

import java.time.Instant;
import java.util.UUID;

public class Habit {

    private String uuid, habitName, frequency;
    private long createdDate;
    private int currStreak;

    public Habit() {
    }

    public Habit(String habitName, String frequency) {
        this.habitName = habitName;
        this.frequency = frequency;
        this.currStreak = 0;
        this.uuid = UUID.randomUUID().toString();
        this.createdDate = Instant.now().toEpochMilli();
    }

    public String getHabitName() {
        return habitName;
    }

    public String getFrequency() {
        return frequency;
    }

    public int getCurrStreak() {
        return currStreak;
    }

    public String getUuid() {
        return uuid;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    // TODO: this class is important - design how to use this effectively
}
