package szu.dky.clockcalendar.service;

import java.lang.Class;

public enum ServiceName {
    ROUTER(szu.dky.clockcalendar.service.router.RouterAPI.class),
    DATETIME(szu.dky.clockcalendar.service.datetime.DatetimeAPI.class),
    COUNTDOWN(szu.dky.clockcalendar.service.countdown.CountdownAPI.class),
    HABIT(szu.dky.clockcalendar.service.habit.HabitAPI.class),
    TODO(szu.dky.clockcalendar.service.todo.TodoAPI.class);

    private final Class<?> SERVICE_CLASS;

    ServiceName(Class<?> clazz) {
        this.SERVICE_CLASS = clazz;
    }

    public Class<?> getServiceClass() {
        return this.SERVICE_CLASS;
    }
}