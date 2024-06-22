package szu.dky.clockcalendar.service.datetime;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatetimeAPI extends JavascriptObject {

    static final Calendar calendar = new Calendar();
    static final Clock clock = new Clock();

    public static JavascriptObject getService() {
        return new DatetimeAPI();
    }

    @GetMapping("/datetime/getDate")
    @JavascriptFunction
    public String getDate() {
        return this.calendar.getDate();
    }

    @PostMapping("/datetime/setDate")
    @JavascriptFunction
    public String setDate(@RequestBody String dateString) {
        this.calendar.setDate(dateString);
        return this.calendar.getDate();
    }

    @PostMapping("/datetime/importCalendar")
    @JavascriptFunction
    public String importCalendar(@RequestBody String path) {
        this.calendar.importCalendar(path);
        return this.calendar.getDate();
    }

    @GetMapping("/datetime/getTime")
    @JavascriptFunction
    public String getTime() {
        return this.clock.getTime();
    }

    private static class TimeParams {
        public Integer hour;
        public Integer minute;
    }

    @PostMapping("/datetime/setTime")
    @JavascriptFunction
    // public void setTime(@RequestBody Integer hour, @RequestBody Integer minute) {
    public void setTime(@RequestBody TimeParams time) {
        int hour = time.hour;
        int minute = time.minute;
        this.clock.setTime(hour, minute, 0);
    }

    @PostMapping("/datetime/syncTime")
    @JavascriptFunction
    public void syncTime(@RequestBody String ntpServer) {
        this.clock.syncTime(ntpServer);
    }

    public void shutdown() {
        this.calendar.shutdown();
        this.clock.shutdown();
    }

}