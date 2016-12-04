package dev.saxionroosters.general;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;

import dev.saxionroosters.BuildConfig;
import dev.saxionroosters.ScheduleDeserializer;
import dev.saxionroosters.SearchResultDeserializer;
import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.model.SearchResult;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by jelle on 27/11/2016.
 *
 * General methods
 */
public class Utils {

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


    /**
     * Parses an errorBody from the response of a RetroFit call
     * @param response
     * @return the parsed Error in an ErrorEvent object.
     */
    public static ErrorEvent parseError(Response<?> response) {
        try {
            Converter<ResponseBody, ErrorEvent> converter =
                    ServiceGenerator.getRetrofit().responseBodyConverter(ErrorEvent.class, new Annotation[0]);

            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorEvent();
        }
    }

    /**
     * Used to parse the received date to a calendar (example: 2016-12-01)
     * @return SimpleDateFormat that parses a string.
     */
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("yyyy-M-dd");
    }

    /**
     * Used to parse the received date to a calendar (example: Friday 1 December 2016)
     * @return SimpleDateFormat that parses a string.
     */
    public static SimpleDateFormat getFullDateFormatter() {
        return new SimpleDateFormat("EEEE dd MMMM yyyy");
    }

}
