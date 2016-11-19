package dev.saxionroosters.extras;

import android.util.Log;

/**
 * Created by Doppie on 4-3-2016.
 */
public class Tools {

    public static boolean logging = true;

    public static void log(String message) {
        if(logging) Log.e("SaxionRoosters", message);
    }
}
