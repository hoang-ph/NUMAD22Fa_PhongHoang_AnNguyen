package edu.northeastern.nowornever.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import edu.northeastern.nowornever.R;

public class Constants {

    public static final String
            ROOT = "users",
            USERNAME_KEY = "usernameKey",
            STICKER_STORAGE = "receivedStickers";

    // For Habit Tracking Activity
    public static final String
            ROOT_HABIT = "habitUsers",
            CHILD_HABIT = "habitsList",
            SHARED_PREF = "userPref",
            SUCCESS_LOGIN = "Successfully logged in",
            SUCCESS_LOGOUT = "Successfully logged out",
            SUCCESS_SIGNUP = "Successfully signed up",
            SUCCESS_ADD = "Successfully added",
            SUCCESS_DELETE = "Successfully deleted",
            INVALID_CREDENTIAL = "Either username doesn't exist or password doesn't match",
            RETRIEVAL_FAILURE = "Failed to retrieve data",
            DELETE_FAILURE = "Failed to delete",
            USER_EXISTS = "Username already exists, try again",
            BLANK_NOTI = "Both fields cannot be blank",
            HABIT_ID_KEY = "habitIdKey",
            SERVER_ERROR = "Server error - fail to get existing data",
            CHILD_NOTE = "notesList",
            NOTE_TITLE_KEY = "noteTitleKey",
            NOTE_DESCRIPTION_KEY = "noteDescriptionKey",
            NOTE_USERNAME_KEY = "noteUsernameKey",
            NOTE_HABIT_UUID_KEY = "noteHabitUuidKey",
            NOTE_UUID_KEY = "NOTE_UUID_KEY";

    public static final long ONE_DAY_IN_EPOCH = 86500000L;

    // Home Fragment
    public static final SimpleDateFormat CALENDAR_TITLE_FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    public static final String
            FUTURE_DATE_CHECKED_ERROR = "Can't complete future date",
            CHILD_COMPLETION = "dailyCompletion";

    // Media Fragment
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());

    public static final String
            UPLOAD = "Upload Image",
            LOADING = "Loading.....",
            SUCCESS_UPLOAD = "Successfully uploaded",
            FAIL_UPLOAD = "Failed to upload image",
            CHILD_HABIT_IMG = "HabitImagesList",
            NO_IMG_NOTI = "An image has to be selected";

    // Stat Fragment
    public static final String
            CALENDAR_TITLE = "calendarTitle",
            MONTHLY_PERCENTAGE = "monthPercentage";

    // Reference here: https://developer.android.com/reference/android/R.drawable.html
    public static final Map<String, Integer> STICKER_RESOURCES = Map.of(
            "Star", R.drawable.star,
            "Microphone", R.drawable.microphone,
            "Music", R.drawable.music,
            "Phone", R.drawable.phone,
            "Bluetooth", R.drawable.bluetooth
    );
}
