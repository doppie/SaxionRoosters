package dev.saxionroosters.eventbus;

import dev.saxionroosters.model.Schedule;

/**
 * Created by jelle on 27/11/2016.
 *
 * Used whenever you want to push a Schedule through EventBus to a registered Eventbus listener.
 */
public class ScheduleEvent {

    private String group;
    private int offset;
    private Schedule schedule;

    public ScheduleEvent(String group, int offset, Schedule schedule) {
        this.group = group;
        this.offset = offset;
        this.schedule = schedule;
    }

    public String getGroup() {
        return group;
    }

    public int getOffset() {
        return offset;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
