package edu.northeastern.nowornever.utils;

import java.util.Map;

import edu.northeastern.nowornever.R;

public class Constants {

    public static final String
            ROOT = "users",
            USERNAME_KEY = "usernameKey",
            STICKER_STORAGE = "receivedStickers";

    // Reference here: https://developer.android.com/reference/android/R.drawable.html
    public static final Map<String, Integer> STICKER_RESOURCES = Map.of(
            "Star", R.drawable.star,
            "Microphone", R.drawable.microphone,
            "Music", R.drawable.music,
            "Phone", R.drawable.phone,
            "Bluetooth", R.drawable.bluetooth
    );
}
