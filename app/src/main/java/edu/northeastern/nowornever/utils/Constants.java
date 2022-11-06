package edu.northeastern.nowornever.utils;

import java.util.Map;

public class Constants {

    public static final String
            ROOT = "users",
            USERNAME_KEY = "usernameKey",
            STICKER_STORAGE = "receivedStickers";

    // Reference here: https://developer.android.com/reference/android/R.drawable.html
    public static final Map<String, Integer> STICKER_RESOURCES = Map.of(
            "Star", 17301516,
            "Microphone", 17301681,
            "Music", 17301635,
            "Phone", 17301638,
            "Bluetooth", 17301632
    );
}
