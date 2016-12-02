package dev.saxionroosters.schedulelist;

import dev.saxionroosters.general.IPresenter;

/**
 * Created by jelle on 27/11/2016.
 */

public interface IScheduleListPresenter extends IPresenter {


    /**
     * Calls the interactor to request a schedule for the current group and week.
     */
    void getSchedule();


}
