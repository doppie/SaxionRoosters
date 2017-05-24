package dev.saxionroosters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import dev.saxionroosters.model.Schedule;
/**
 * Created by jelle on 30/11/2016.
 */

public class ScheduleDeserializer implements JsonDeserializer<Schedule> {

    @Override
    public Schedule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();

        Schedule schedule = gson.fromJson(json, Schedule.class);

        return schedule;
    }
}