package szu.dky.clockcalendar.service.habit;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.config.DataConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitAPI extends JavascriptObject {

    List<Habit> habits;
    private static final String DEFAULT_PATH = "/habits.json";

    public static JavascriptObject getService() {
        return new HabitAPI();
    }

    public HabitAPI() {
        habits = loadHabits(DataConfig.DATA_DIR + DEFAULT_PATH);
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

    @GetMapping("/habit/getHabits")
    @JavascriptFunction
    public String getHabits() {
        StringBuilder htmlBuilder = new StringBuilder();

        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            String freqText = "";
            switch (habit.freq) {
                case DAILY:
                    freqText = "每日";
                    break;
                case WEEKLY:
                    freqText = "每周";
                    break;
                case MONTHLY:
                    freqText = "每月";
                    break;
            }

            htmlBuilder.append("<div class=\"habit\" onclick=\"getStastics(").append(i).append(")\">\n")
                    .append("    <span class=\"habit-name\">").append(habit.name).append(" (").append(freqText).append(")</span>")
                    .append("    <span class=\"habit-progress\">").append(habit.checkins.length).append("次</span>\n")
                    .append("    <span class=\"habit-start-date\">").append(habit.getStartDate()).append("</span>\n")
                    .append("    <button class=\"button\" onclick=\"checkin(").append(i).append(")\">打卡</button>\n")
                    .append("</div>\n");
        }

        return htmlBuilder.toString();
    }

    private static class HabitParams {
        public String name;
        public String freq;
        public String startDate;
    }

    @PostMapping("/habit/addHabit")
    @JavascriptFunction
    public String addHabit(@RequestBody HabitParams params) {
        String name = params.name;
        String freq = params.freq;
        String startDate = params.startDate;
        try {
            habits.add(new Habit().name(name).freq(freq).startDate(startDate));
            saveHabits(DataConfig.DATA_DIR + DEFAULT_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            UI.getInstance().eval("alert('参数非法')");
        }
        return getHabits();
    }

    @PostMapping("/habit/deleteHabit")
    @JavascriptFunction
    public String deleteHabit(@RequestBody int index) {
        try {
            habits.remove(index);
        } catch (Exception e) {
        }
        saveHabits(DataConfig.DATA_DIR + DEFAULT_PATH);
        return getHabits();
    }

    @PostMapping("/habit/checkin")
    @JavascriptFunction
    public String checkin(@RequestBody int index) {
        Habit habit = habits.get(index);
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // 检查今天是否已经打卡
        if (habit.checkins.length > 0) {
            Date checkinDate = habit.checkins[habit.checkins.length - 1];
            Calendar checkinCalendar = Calendar.getInstance();
            checkinCalendar.setTime(checkinDate);
            if (checkinCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                checkinCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                UI.getInstance().eval("alert('今日已打卡！')");
                return getHabits(); // 今天已经打卡，直接返回
            }
        }

        // 检查今天是否是合法的打卡日
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(habit.startDate);

        boolean isValidCheckinDay = false;
        switch (habit.freq) {
            case DAILY:
                isValidCheckinDay = true; // 每日打卡，任何一天都是合法的打卡日
                break;
            case WEEKLY:
                int daysBetween = (int) ((today.getTime() - habit.startDate.getTime()) / (1000 * 60 * 60 * 24));
                isValidCheckinDay = (daysBetween % 7 == 0); // 每周打卡，检查是否是 7 的倍数天
                break;
            case MONTHLY:
                isValidCheckinDay = (calendar.get(Calendar.DAY_OF_MONTH) == startCalendar.get(Calendar.DAY_OF_MONTH));
                break;
        }

        if (isValidCheckinDay) {
            // 检查上次打卡日是否是合法的打卡日
            boolean isLastCheckinValid = false;
            if (habit.checkins.length > 0) {
                Date lastCheckinDate = habit.checkins[habit.checkins.length - 1];
                Calendar lastCheckinCalendar = Calendar.getInstance();
                lastCheckinCalendar.setTime(lastCheckinDate);

                switch (habit.freq) {
                    case DAILY:
                        lastCheckinCalendar.add(Calendar.DAY_OF_YEAR, 1);
                        isLastCheckinValid = (lastCheckinCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                            lastCheckinCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR));
                        break;
                    case WEEKLY:
                        lastCheckinCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                        isLastCheckinValid = (lastCheckinCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                            lastCheckinCalendar.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR));
                        break;
                    case MONTHLY:
                        lastCheckinCalendar.add(Calendar.MONTH, 1);
                        isLastCheckinValid = (lastCheckinCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                                            lastCheckinCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH));
                        break;
                }
            }

            if (isLastCheckinValid) {
                habit.chain++;
            } else {
                habit.chain = 1; // 重新开始计数
            }

            // 添加今天的打卡记录
            Date[] newCheckins = new Date[habit.checkins.length + 1];
            System.arraycopy(habit.checkins, 0, newCheckins, 0, habit.checkins.length);
            newCheckins[habit.checkins.length] = today;
            habit.checkins = newCheckins;
        } else {
            UI.getInstance().eval("alert('今天不是打卡日！')");
        }
        saveHabits(DataConfig.DATA_DIR + DEFAULT_PATH);
        return getHabits();
    }

    @PostMapping("/habit/getStastics")
    @JavascriptFunction
    public String getStastics(@RequestBody int index) {
        Habit habit = habits.get(index);
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<p>习惯：").append(habit.name).append("</p>\n");
        htmlBuilder.append("<p>总打卡次数：").append(habit.checkins.length).append("</p>\n");
        htmlBuilder.append("<p>连续打卡：").append(habit.chain).append("</p>\n");
        return htmlBuilder.toString();
    }

    @GetMapping("/habit/getShouldCheckinToday")
    @JavascriptFunction
    public String getShouldCheckinToday() {
        StringBuilder noticeBuilder = new StringBuilder();
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        for (Habit habit : habits) {
            // 检查今天是否是合法的打卡日
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(habit.startDate);

            boolean isValidCheckinDay = false;
            switch (habit.freq) {
                case DAILY:
                    isValidCheckinDay = true; // 每日打卡，任何一天都是合法的打卡日
                    break;
                case WEEKLY:
                    int daysBetween = (int) ((today.getTime() - habit.startDate.getTime()) / (1000 * 60 * 60 * 24));
                    isValidCheckinDay = (daysBetween % 7 == 0); // 每周打卡，检查是否是 7 的倍数天
                    break;
                case MONTHLY:
                    isValidCheckinDay = (calendar.get(Calendar.DAY_OF_MONTH) == startCalendar.get(Calendar.DAY_OF_MONTH));
                    break;
            }
            boolean isCheckedToday = false;
            if (isValidCheckinDay && habit.checkins.length > 0) {
                // 检查今天是否已经打卡
                Date checkinDate = habit.checkins[habit.checkins.length - 1];
                Calendar checkinCalendar = Calendar.getInstance();
                checkinCalendar.setTime(checkinDate);
                if (checkinCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    checkinCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                    
                    isCheckedToday = true;
                }
            }
            if (isValidCheckinDay && !isCheckedToday) {
                noticeBuilder.append(habit.name + "\n");
            }
        }
        return noticeBuilder.toString();
    }

    public void saveHabits(String path) {
        String json = JSON.toJSONString(habits);
        try {
            Files.write(Path.of(path), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
    }

}