package szu.dky.clockcalendar.service.countdown;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.stream.Collectors;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import java.util.Date;
import java.util.Calendar;
import szu.dky.clockcalendar.util.Beeper;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.config.DataConfig;

public class CountdownAPI extends JavascriptObject {

    private Timer pomodoroTimer = new Timer();
    private Timer countdownTimer = new Timer();
    private TimerTask pomodoroTask;
    private TimerTask countdownTask;
    private static int phase; // 番茄钟偶数工作，奇数休息
    private Map<String, Integer> historyPomodoros;
    private static final String DEFAULT_PATH = "/pomodoro.json";
    private long pomodoroEndTime;
    private long countdownEndTime;

    public static JavascriptObject getService() {
        return new CountdownAPI();
    }

    public CountdownAPI() {
        this.historyPomodoros = loadPomodoros(DataConfig.DATA_DIR + DEFAULT_PATH);
    }

    @JavascriptFunction
    public String getHistoryPomodoros(String path) {
        return JSON.toJSONString(historyPomodoros);
    }

    @JavascriptFunction
    public void startPomodoro() {
        if (pomodoroTask != null) {
            UI.getInstance().eval("alert('番茄钟进行中')");
            return;
        }
        phase = 0;
        startPomodoroPhase();
    }

    private void startPomodoroPhase() {
        int duration = 60000;
        if (phase % 2 == 0) {
            duration *= 25; // 25 minutes for work
        } else {
            duration *= phase == 7 ? 25 : 5; // 25 minutes for long break, 5 minutes for short break
        }

        long pomodoroStartTime = System.currentTimeMillis();
        pomodoroEndTime = pomodoroStartTime + duration;

        pomodoroTask = new TimerTask() {
            @Override
            public void run() {
                Beeper.ringBell();
                if (phase % 2 == 0) {
                    incrementPomodoroCount();
                }
                phase = (phase + 1) % 8;
                pomodoroTask = null;
                startPomodoroPhase();
            }
        };

        pomodoroTimer.schedule(pomodoroTask, duration);
    }

    @JavascriptFunction
    public String getPomodoroStatus() {
        long remainingSec = (pomodoroEndTime - System.currentTimeMillis()) / 1000;
        if (remainingSec < 0) {
            remainingSec = 0;
        }
        String status = (phase % 2 == 0) ? "工作中" : "休息中";
        return JSON.toJSONString(Map.of("status", status, "remainingSec", remainingSec, "nextBreak", ((phase + 1) / 2 == 3) ? 25 : 5));
    }

    @JavascriptFunction
    public int getPomodoroCount() {
        String today = getCurrentDate();
        return historyPomodoros.getOrDefault(today, 0);
    }

    @JavascriptFunction
    public void stopPomodoro() {
        if (pomodoroTask != null) {
            pomodoroTask.cancel();
            pomodoroEndTime = 0;
            pomodoroTask = null;
            UI.getInstance().eval("clearInterval(pomodoroPoll);");
        }
        phase = 0;
    }

    @JavascriptFunction
    public void startCountdown(int seconds) {
        if (seconds <= 0) {
            UI.getInstance().eval("alert('非法时长')");
            return;
        }
        if (countdownTask != null) {
            UI.getInstance().eval("alert('已在倒计时')");
            return;
        }
        long countdownStartTime = System.currentTimeMillis();
        int ms = seconds * 1000;
        countdownEndTime = countdownStartTime + ms;

        countdownTask = new TimerTask() {
            @Override
            public void run() {
                UI.getInstance().eval("clearInterval(countdownPoll);");
                Beeper.ringBell();
                countdownTask = null;
            }
        };

        countdownTimer.schedule(countdownTask, ms);
    }

    @JavascriptFunction
    public void stopCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownEndTime = 0;
            countdownTask = null;
            UI.getInstance().eval("clearInterval(countdownPoll);");
        }
    }

    @JavascriptFunction
    public String getCountdownStatus() {
        long remainingSec = (countdownEndTime - System.currentTimeMillis()) / 1000;
        if (remainingSec < 0) {
            remainingSec = 0;
        }
        return JSON.toJSONString(Map.of("remainingSec", remainingSec));
    }

    private void incrementPomodoroCount() {
        String today = getCurrentDate();
        int count = historyPomodoros.getOrDefault(today, 0) + 1;
        historyPomodoros.put(today, count);
        UI.getInstance().eval("document.getElementById('pomodoro-count').innerHTML = '完成番茄数：" + count + "';");
        savePomodoros(DataConfig.DATA_DIR + DEFAULT_PATH);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public Map<String, Integer> loadPomodoros(String path) {
        String serializedList = "";
        try {
            serializedList = Files.readString(Path.of(path));
            return JSON.parseArray(serializedList, PomodoroRecord.class, JSONReader.Feature.SupportSmartMatch)
                       .stream()
                       .collect(Collectors.toMap(pr -> pr.date, pr -> pr.count, (existing, replacement) -> existing));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public void savePomodoros(String path) {
        String json = JSON.toJSONString(historyPomodoros.entrySet().stream()
            .map(entry -> new PomodoroRecord().date(entry.getKey()).count(entry.getValue()))
            .collect(Collectors.toList())
        );
        try {
            Files.write(Path.of(path), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        pomodoroTimer.cancel();
        countdownTimer.cancel();
    }

}