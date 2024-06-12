package szu.dky.clockcalendar.service.countdown;

import dev.webview.webview_java.bridge.JavascriptObject;
import java.util.Timer;
import java.util.TimerTask;
import szu.dky.clockcalendar.util.Beeper;

public class Main extends JavascriptObject {

    private Timer timer = new Timer();
    private TimerTask callback = new TimerTask() {
        @Override
        public void run() {
            Beeper.ringBell();
        }
    };

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
    }

    public void startPomodoro() {

    }

    public void stopPomodoro() {

    }

    public void startCountdown(int seconds) {
        timer.schedule(callback, seconds * 1000);
    }

    public void stopCountdown() {
        callback.cancel();
    }

}