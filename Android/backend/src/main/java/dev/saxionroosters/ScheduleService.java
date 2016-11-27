package dev.saxionroosters;

import java.util.List;

import dev.saxionroosters.model.Schedule;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jelle on 27/11/2016.
 */

public interface ScheduleService {

    @GET("/v2/groups/schedule.json")
    Call<Schedule> getScheduleForGroup(@Query("group") String group, @Query("week") int week);

    @GET("/v2/teachers/schedule.json")
    Call<Schedule> getScheduleForTeacher(@Query("teacher") String group, @Query("week") int week);

}
