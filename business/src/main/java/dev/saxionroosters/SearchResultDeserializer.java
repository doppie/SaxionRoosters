package dev.saxionroosters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import dev.saxionroosters.model.SearchResult;

/**
 * Created by jelle on 29/11/2016.
 */

public class SearchResultDeserializer implements JsonDeserializer<SearchResult> {

    @Override
    public SearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonElement value = json.getAsJsonObject().get("result");

        SearchResult result = gson.fromJson(value, SearchResult.class);

        return result;
    }
}
