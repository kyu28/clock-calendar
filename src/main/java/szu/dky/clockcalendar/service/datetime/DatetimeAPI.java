package szu.dky.clockcalendar.service.datetime;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;

public class DatetimeAPI extends JavascriptObject {

    static final Calendar calendar = new Calendar();
    static final Clock clock = new Clock();

    public static JavascriptObject getService() {
        return new DatetimeAPI();
    }

    @JavascriptFunction
    public String getDate() {
        return this.calendar.getDate();
    }

    @JavascriptFunction
    public String setDate(String dateString) {
        this.calendar.setDate(dateString);
        return this.calendar.getDate();
    }

    @JavascriptFunction
    public String importCalendar(String path) {
        this.calendar.importCalendar(path);
        return this.calendar.getDate();
    }

    @JavascriptFunction
    public String getTime() {
        return this.clock.getTime();
    }

    @JavascriptFunction
    public void setTime(Integer hour, Integer minute) {
        this.clock.setTime(hour, minute, 0);
    }

    @JavascriptFunction
    public void syncTime(String ntpServer) {
        this.clock.syncTime(ntpServer);
    }

    public void shutdown() {
        this.calendar.shutdown();
        this.clock.shutdown();
    }

}