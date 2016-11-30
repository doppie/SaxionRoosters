package dev.saxionroosters.general;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.saxionroosters.BuildConfig;
import dev.saxionroosters.ScheduleDeserializer;
import dev.saxionroosters.SearchResultDeserializer;
import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.model.SearchResult;

/**
 * Created by jelle on 27/11/2016.
 *
 * General methods
 */
public class Tools {

    public static void log(String message) {
        if(BuildConfig.DEBUG) {
            Log.e("Roosters_MVP", message);
        }
    }

    public static void log(String tag, String message) {
        if(BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }

    /**
     * @return a Gson instance with a custom deserializer for the SearchResult object.
     */
    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(SearchResult.class, new SearchResultDeserializer())
                .registerTypeAdapter(Schedule.class, new ScheduleDeserializer())
                .create();
        return gson;
    }

}
