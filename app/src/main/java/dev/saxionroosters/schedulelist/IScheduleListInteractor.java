package dev.saxionroosters.schedulelist;

/**
 * Created by jelle on 27/11/2016.
 */

public interface IScheduleListInteractor {

    /**
     * Handles the call to the Rooster API through RetroFit.
     * @param group
     * @param week
     */
    void getScheduleForGroup(String group, int week);

}
