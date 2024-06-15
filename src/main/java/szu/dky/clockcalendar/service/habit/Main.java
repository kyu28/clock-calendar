package szu.dky.clockcalendar.service.habit;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

public class Main extends JavascriptObject {

    List<Habit> habits;
    private static final String DEFAULT_PATH = "./habits.json";

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
        habits = loadHabits(DEFAULT_PATH);
    }

    private List<Habit> loadHabits(String path) {
        try {
            String serializedList = Files.readString(Path.of(path));
            return JSON.parseArray(serializedList, Habit.class, JSONReader.Feature.SupportSmartMatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Habit>();
    }

    @JavascriptFunction
    public String getHabits() {
        // TODO: return HTML;
        return "";
    }

    @JavascriptFunction
    public void addHabit() {
        // TODO
    }

    @JavascriptFunction
    public String deleteHabit(int index) {
        habits.remove(index);
        return getHabits();
    }

    @JavascriptFunction
    public void checkin() {

    }

}