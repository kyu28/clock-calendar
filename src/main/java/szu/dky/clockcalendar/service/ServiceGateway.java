package szu.dky.clockcalendar.service;

import dev.webview.webview_java.bridge.JavascriptObject;
import java.util.Map;
import java.util.EnumMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;

public class ServiceGateway {

    private static final ServiceGateway instance = new ServiceGateway();
    private Map<ServiceName, JavascriptObject> services;

    private ServiceGateway() {
        this.services = new EnumMap<>(ServiceName.class);
    }

    public static ServiceGateway getInstance() {
        return instance;
    }

    public JavascriptObject getService(ServiceName serviceName) {
        if (services.get(serviceName) == null) {
            Class<?> serviceClass = serviceName.getServiceClass();
            try {
                Method factoryMethod = serviceClass.getMethod("getService");
                services.put(serviceName, (JavascriptObject)factoryMethod.invoke(null));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this.services.get(serviceName);
    }

    public void shutdownAll() {
        for (JavascriptObject service : services.values()) {
            try {
                Class<?> serviceClass = service.getClass();
                Method shutdownMethod = serviceClass.getMethod("shutdown");
                shutdownMethod.invoke(service, null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}