package szu.dky.clockcalendar.service.datetime;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;

public class Main extends JavascriptObject {

    static final Calendar calendar = new Calendar();
    static final Clock clock = new Clock();

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
    }

    @JavascriptFunction
    public String getDate() {
        return this.calendar.getDate();
    }

    @JavascriptFunction
    public void setDate(String dateString) {
        this.calendar.setDate(dateString);
    }

    @JavascriptFunction
    public void importCalendar(String path) {
        this.calendar.importCalendar(path);
    }

    @JavascriptFunction
    public String getTime() {
        return this.clock.getTime();
    }

    @JavascriptFunction
    public void setTime(int hour, int minute, int second) {
        this.clock.setTime(hour, minute, second);
    }

    @JavascriptFunction
    public void syncTime(String ntpServer) {
        this.clock.syncTime(ntpServer);
    }

}