package dev.saxionroosters.general;

import android.util.Log;

import dev.saxionroosters.BuildConfig;

/**
 * Created by jelle on 27/11/2016.
 */

public class Tools {

    public static void log(String message) {
        if(BuildConfig.DEBUG) {
            Log.e("Roosters_MVP", message);
        }
    }
}
