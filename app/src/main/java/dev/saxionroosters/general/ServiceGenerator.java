package dev.saxionroosters.general;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jelle on 30/11/2016.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "http://api.roosters.saxion.nl/";

    private static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(Utils.getGson()))
                    .build();

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}