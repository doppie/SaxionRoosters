package dev.saxionroosters.general;

import java.io.IOException;
import java.lang.annotation.Annotation;

import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.general.ServiceGenerator;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by jelle on 30/11/2016.
 */

public class ErrorUtils {

    public static ErrorEvent parseError(Response<?> response) {
        try {
            Converter<ResponseBody, ErrorEvent> converter =
                    ServiceGenerator.getRetrofit().responseBodyConverter(ErrorEvent.class, new Annotation[0]);

            return converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorEvent();
        }
    }
}
