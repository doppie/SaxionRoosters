package dev.saxionroosters.schedulelist;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.eventbus.ScheduleEvent;

/**
 * Created by jelle on 27/11/2016.
 */

public class ScheduleListPresenter implements IScheduleListPresenter {

    private ScheduleListInteractor interactor;
    private ScheduleListView view;

    private String group;
    private int week;

    public ScheduleListPresenter(ScheduleListView view, String group, int week) {
        this.view = view;
        this.group = group;
        this.week = week;

        interactor = new ScheduleListInteractor();
    }

    @Override
    public void getSchedule() {
        interactor.getScheduleForGroup(group, week);
        view.dismissRetryLayout();
        view.showLoadingLayout();
    }

    @Override
    public void resume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void pause() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScheduleReceived(ScheduleEvent event) {

        //we might receive an event from another fragment
        //so we make sure the event is for us by checking the week and group.
        if(event.getGroup().equals(group) && event.getWeek() == week) {
            view.showSchedule(event.getSchedule());
            view.dismissLoadingLayout();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorReceived(ErrorEvent event) {
        view.dismissLoadingLayout();
        view.showRetryLayout();
        view.showMessage(event.getError().toString());
    }

}
