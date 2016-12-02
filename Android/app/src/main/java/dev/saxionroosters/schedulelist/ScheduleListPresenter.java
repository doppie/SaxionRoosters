package dev.saxionroosters.schedulelist;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import dev.saxionroosters.eventbus.ErrorEvent;
import dev.saxionroosters.eventbus.ScheduleEvent;
import dev.saxionroosters.general.Utils;

/**
 * Created by jelle on 27/11/2016.
 */

public class ScheduleListPresenter implements IScheduleListPresenter {

    private ScheduleListInteractor interactor;
    private ScheduleListView view;

    private String group;
    private int offset;

    public ScheduleListPresenter(ScheduleListView view, String group, int offset) {
        this.view = view;
        this.group = group;
        this.offset = offset;
        Utils.log("Offset: " + offset);
        interactor = new ScheduleListInteractor();
    }

    @Override
    public void getSchedule() {
        view.showLoadingLayout();
        view.dismissRetryLayout();
        interactor.getScheduleForGroup(group, offset);
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
        //so we make sure the event is for us by checking the offset and group.
        if(event.getGroup().equalsIgnoreCase(group) && event.getOffset() == offset) {
            view.dismissLoadingLayout();
            view.showSchedule(event.getSchedule());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorReceived(ErrorEvent event) {
        Utils.log("[Error] " + event.getMessage());
        view.dismissLoadingLayout();
        view.showRetryLayout();
        view.showMessage(event.getMessage());
    }

}
