package szu.dky.clockcalendar.service.countdown;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import szu.dky.clockcalendar.util.Beeper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

public class Main extends JavascriptObject {

    private Timer timer = new Timer();
    private TimerTask callback = new TimerTask() {
        @Override
        public void run() {
            Beeper.ringBell();
        }
    };

    private int pomodoros;
    private List<PomodoroRecord> historyPomodoros;
    private static final String DEFAULT_PATH = "../mock/pomodoro.json";

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
        this.historyPomodoros = loadPomodoros(DEFAULT_PATH);
    }

    @JavascriptFunction
    public String getHistoryPomodoros(String path) {
        return JSON.toJSONString(historyPomodoros);
    }

    @JavascriptFunction
    public void startPomodoro() {

    }

    @JavascriptFunction
    public void stopPomodoro() {

    }

    @JavascriptFunction
    public void startCountdown(int seconds) {
        timer.schedule(callback, seconds * 1000);
    }

    @JavascriptFunction
    public void stopCountdown() {
        callback.cancel();
    }

    public List<PomodoroRecord> loadPomodoros(String path) {
        String serializedList = "";
        try {
            serializedList = Files.readString(Path.of(path));
            return JSON.parseArray(serializedList, PomodoroRecord.class, JSONReader.Feature.SupportSmartMatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void savePomodoros(String path) {
        String json = JSON.toJSONString(historyPomodoros);
        try {
            Files.write(Path.of(path), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}