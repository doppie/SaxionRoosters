package dev.saxionroosters.schedulelist;

import org.greenrobot.eventbus.EventBus;

import dev.saxionroosters.ScheduleService;
import dev.saxionroosters.general.Tools;
import dev.saxionroosters.eventbus.ScheduleEvent;
import dev.saxionroosters.model.Schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jelle on 27/11/2016.
 */

public class ScheduleListInteractor implements IScheduleListInteractor {

    private ScheduleService service;
    private Retrofit retrofit;

    public ScheduleListInteractor() {

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.roosters.saxion.nl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ScheduleService.class);
    }

    @Override
    public void getScheduleForGroup(final String group, final int week) {
        final Call<Schedule> schedule = service.getScheduleForGroup("EIB1a", 0);
        schedule.enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                Tools.log("Received schedule!");

                //notify the presenter with the data
                EventBus.getDefault().post(new ScheduleEvent(group, week, response.body()));
            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                Tools.log("Failure: " + t.toString());
            }
        });
    }
}
