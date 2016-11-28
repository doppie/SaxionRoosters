package dev.saxionroosters.schedulelist;

import java.util.ArrayList;

import dev.saxionroosters.general.IView;
import dev.saxionroosters.model.Schedule;

/**
 * Created by jelle on 27/11/2016.
 */

public interface ScheduleListView extends IView {

    /**
     * Updates the UI with a schedule.
     * @param schedule the schedule to be shown.
     */
    void showSchedule(Schedule schedule);

    /**
     * Show a loading layout
     */
    void showLoadingLayout();

    /**
     * If we could not load the schedule we can show a retry layout
     * with a button so the user can manually retry to load.
     */
    void showRetryLayout();

    /**
     * Dismiss the loading layout.
     */
    void dismissLoadingLayout();

    /**
     * Dismiss the retry layout.
     */
    void dismissRetryLayout();

    /**
     * Show a snackbar message to the user.
     * @param message
     */
    void showMessage(String message);

}
