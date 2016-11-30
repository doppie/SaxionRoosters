package dev.saxionroosters.general;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jelle on 30/11/2016.
 */

public class PreferenceManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    private static PreferenceManager instance;

    public static PreferenceManager getInstance(Context context) {
        if(instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }

    private PreferenceManager(Context context) {
        prefs = context.getSharedPreferences("roosters", Activity.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    /**
     * Stores a value to the SharedPrefs
     * @param key
     * @param value
     */
    public void write(Prefs key, String value) {
        prefsEditor.putString(key.toString(), value);
        prefsEditor.apply();
    }

    /**
     * Reads a value from the SharedPrefs
     * @param key
     * @return
     */
    public String read(Prefs key) {
        return prefs.getString(key.toString(), "");
    }
}
