package szu.dky.clockcalendar.service;

import java.lang.Class;

public enum ServiceName {
    BEEP(szu.dky.clockcalendar.service.beep.Main.class),
    ROUTER(szu.dky.clockcalendar.service.router.Main.class);

    private final Class<?> SERVICE_CLASS;

    ServiceName(Class<?> clazz) {
        this.SERVICE_CLASS = clazz;
    }

    public Class<?> getServiceClass() {
        return this.SERVICE_CLASS;
    }
}