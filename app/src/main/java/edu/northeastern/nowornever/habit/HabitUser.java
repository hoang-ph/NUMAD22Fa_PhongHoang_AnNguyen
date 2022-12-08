package edu.northeastern.nowornever.habit;

public class HabitUser {

    private String username, password;

    public HabitUser() {
    }

    public HabitUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}
