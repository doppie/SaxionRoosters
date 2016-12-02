package dev.saxionroosters.schedulelist;

import org.greenrobot.eventbus.EventBus;

import dev.saxionroosters.Dataset;
import dev.saxionroosters.ScheduleRepository;
import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.general.ServiceGenerator;
import dev.saxionroosters.eventbus.ScheduleEvent;
import dev.saxionroosters.general.Utils;
import dev.saxionroosters.model.Schedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jelle on 27/11/2016.
 */

public class ScheduleListInteractor implements IScheduleListInteractor {

    private ScheduleRepository repository;
    private Dataset dataset;

    public ScheduleListInteractor() {
        repository = ServiceGenerator.createService(ScheduleRepository.class);
        dataset = Dataset.getInstance();
    }

    @Override
    public void getScheduleForGroup(final String group, final int offset) {

        //first check if we already have the schedule.
        Schedule schedule = dataset.getSchedule(group, offset + "");
        if(schedule != null) {
            EventBus.getDefault().post(new ScheduleEvent(group, offset, schedule));
            return;
        }

        //else we make a call to the api
        final Call<Schedule> result = repository.getScheduleForGroup(group, offset);
        result.enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                if(response.isSuccessful()) {
                    //save it in our dataset for reuse.
                    dataset.addSchedule(response.body());

                    //notify the presenter with the data
                    EventBus.getDefault().post(new ScheduleEvent(group, offset, response.body()));
                } else {
                    ErrorEvent errorEvent = Utils.parseError(response);
                    EventBus.getDefault().post(errorEvent);
                }
            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                EventBus.getDefault().post(new ErrorEvent(-1, t.getMessage()));
            }
        });
    }
}
