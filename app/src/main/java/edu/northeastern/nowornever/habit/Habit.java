package edu.northeastern.nowornever.habit;

import java.util.UUID;

import edu.northeastern.nowornever.utils.Utils;

public class Habit {

    private String uuid, habitName, frequency, createdDate;

    public Habit() {
    }

    public Habit(String habitName, String frequency) {
        this.habitName = habitName;
        this.frequency = frequency;
        this.uuid = UUID.randomUUID().toString();
        this.createdDate = Utils.simpleDate();
    }

    public String getHabitName() {
        return habitName;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}
