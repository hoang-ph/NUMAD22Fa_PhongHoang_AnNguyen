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
            CHILD_IMG_ID = "imageIdList",
            SHARED_PREF = "userPref",
            SUCCESS_LOGIN = "Successfully logged in",
            SUCCESS_LOGOUT = "Successfully logged out",
            SUCCESS_SIGNUP = "Successfully signed up",
            SUCCESS_ADD = "Successfully added new habit",
            INVALID_CREDENTIAL = "Either username doesn't exist or password doesn't match",
            RETRIEVAL_FAILURE = "Failed to retrieve data!",
            USER_EXISTS = "Username already exists, try again",
            BLANK_NOTI = "Both fields cannot be blank",
            HABIT_ID_KEY = "habitIdKey",
            SERVER_ERROR = "Server error - fail to get existing data";

    public static final long ONE_DAY_IN_EPOCH = 86500000L;

    // Media Fragment
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault());

    public static final String
            UPLOAD = "Upload Image",
            LOADING = "Loading.....",
            SUCCESS_UPLOAD = "Successfully uploaded",
            FAIL_UPLOAD = "Failed to upload image";

    // Reference here: https://developer.android.com/reference/android/R.drawable.html
    public static final Map<String, Integer> STICKER_RESOURCES = Map.of(
            "Star", R.drawable.star,
            "Microphone", R.drawable.microphone,
            "Music", R.drawable.music,
            "Phone", R.drawable.phone,
            "Bluetooth", R.drawable.bluetooth
    );
}
