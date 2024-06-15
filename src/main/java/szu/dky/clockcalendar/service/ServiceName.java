package szu.dky.clockcalendar.service;

import java.lang.Class;

public enum ServiceName {
    ROUTER(szu.dky.clockcalendar.service.router.Main.class),
    TODO(szu.dky.clockcalendar.service.todo.Main.class);

    private final Class<?> SERVICE_CLASS;

    ServiceName(Class<?> clazz) {
        this.SERVICE_CLASS = clazz;
    }

    public Class<?> getServiceClass() {
        return this.SERVICE_CLASS;
    }
}