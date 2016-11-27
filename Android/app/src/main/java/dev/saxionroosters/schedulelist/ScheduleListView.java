package dev.saxionroosters.schedulelist;

import java.util.ArrayList;

import dev.saxionroosters.model.Schedule;

/**
 * Created by jelle on 27/11/2016.
 */

public interface ScheduleListView {

    /**
     * Updates the UI with this schedule.
     * @param schedule
     */
    void showSchedule(Schedule schedule);

    void showLoadingLayout();

    void showRetryLayout();

    void dismissLoadingLayout();

    void dismissRetryLayout();

    void showMessage(String message);

}
