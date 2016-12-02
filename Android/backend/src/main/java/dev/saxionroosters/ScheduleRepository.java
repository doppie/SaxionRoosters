package dev.saxionroosters;

import dev.saxionroosters.model.Schedule;
import dev.saxionroosters.model.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jelle on 27/11/2016.
 */

public interface ScheduleRepository {

    @GET("/v2/groups/schedule.json")
    Call<Schedule> getScheduleForGroup(@Query("group") String group, @Query("week") int week);

    @GET("/v2/teachers/schedule.json")
    Call<Schedule> getScheduleForTeacher(@Query("teacher") String group, @Query("week") int week);

    @GET("/v2/search.json")
    Call<SearchResult> getSearchResultForQuery(@Query("q") String query);

}
