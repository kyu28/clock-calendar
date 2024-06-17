package szu.dky.clockcalendar.service.datetime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;

public class Calendar {

    private Day monthDays[];
    private int year;
    private int month;
    private int day;
    // for import
    private Map<String, DateObject> offDays;
    private Map<String, DateObject> workingDays;

    public Calendar() {
        // today
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(formatter);
        String[] tokens = dateString.split("-");
        year = Integer.parseInt(tokens[0]);
        month = Integer.parseInt(tokens[1]);
        day = Integer.parseInt(tokens[2]);
        offDays = new HashMap<>();
        workingDays = new HashMap<>();
        this.monthDays = loadMonth();
        importCalendar("../mock/calendar.json");
    }

    public String getDate() {
        StringBuilder jsonBuilder = new StringBuilder();
        String html = generateMonthHTML(this.monthDays).replace("\n", "\\n").replace("\"", "\\\"");
        jsonBuilder.append("{\n");
        jsonBuilder.append(String.format("  \"date\": \"%4d-%02d-%02d\",\n", year, month, day));
        jsonBuilder.append(String.format("  \"html\": \"%s\"\n", html));
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    public void setDate(String dateString) {
        String[] tokens = dateString.split("-");
        year = Integer.parseInt(tokens[0]);
        month = Integer.parseInt(tokens[1]);
        day = Integer.parseInt(tokens[2]);
        monthDays = loadMonth();
    }

    public void importCalendar(String path) {
        class OffDaySetting {
            @JSONField
            public DateObject[] offDays;

            @JSONField
            public DateObject[] workingDays;
        };
        try {
            String json = Files.readString(Path.of(path));
            OffDaySetting offDaySetting = JSON.parseObject(json, OffDaySetting.class);
            Set<DateObject> offDays = Arrays.stream(offDaySetting.offDays).collect(Collectors.toSet());
            Set<DateObject> workingDays = Arrays.stream(offDaySetting.workingDays).collect(Collectors.toSet());
            Set<DateObject> intersection = new HashSet<>(offDays);
            intersection.retainAll(workingDays);
            if (intersection.size() > 0) {
                String dublicated = String.join(", ", (String[])intersection.stream().map(dateObj -> dateObj.date).toArray());
                String be = intersection.size() == 1 ? "is" : "are";
                throw new Exception(String.format("%s %s in both offDays and workingDays", dublicated, be));
            }

            this.offDays = offDays.stream().collect(Collectors.toMap(dateObj -> dateObj.date, dateObj -> dateObj, (existing, replacement) -> existing));
            this.workingDays = workingDays.stream().collect(Collectors.toMap(dateObj -> dateObj.date, dateObj -> dateObj, (existing, replacement) -> existing));
            monthDays = loadMonth();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dateForward() {
        if (++day > monthDays.length) {
            day = 1;
            if (++month > 12) {
                month = 1;
                year++;
            }
            loadMonth();
        }
    }

    public Day[] loadMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int days = 0;
        switch (month) {
        case 1: case 3: case 5: case 7: case 8: case 10: case 12:
            days = 31;
            break;
        case 4: case 6: case 9: case 11:
            days = 30;
            break;
        case 2:
            days = 28;
            if (year % 4 == 0) {
                if (year % 100 == 0) {
                    if (year % 400 == 0) {
                        days++;
                    }
                } else {
                    days++;
                }
            }
            break;
        }
        monthDays = new Day[days];
        for (int day = 1; day <= days; day++) {
            String dateString = String.format("%4d-%02d-%02d", year, month, day);
            LocalDate date = LocalDate.parse(dateString, formatter);
            byte weekday = (byte)date.getDayOfWeek().getValue();
            boolean isOffDay = false;
            String tag = "";
            if (weekday == 6 || weekday == 7) {
                isOffDay = true;
            }
            if (workingDays.containsKey(dateString)) {
                isOffDay = false;
            }
            if (offDays.containsKey(dateString)) {
                isOffDay = true;
                tag = offDays.get(dateString).tag;
            }
            monthDays[day - 1] = new Day().year(year).month(month).day(day).weekday(weekday).isOffDay(isOffDay).tag(tag);
        }
        return monthDays;
    }

    public String generateMonthHTML(Day[] monthDays) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("    <tr>\n");
        htmlBuilder.append("        <th>日</th>\n");
        htmlBuilder.append("        <th>一</th>\n");
        htmlBuilder.append("        <th>二</th>\n");
        htmlBuilder.append("        <th>三</th>\n");
        htmlBuilder.append("        <th>四</th>\n");
        htmlBuilder.append("        <th>五</th>\n");
        htmlBuilder.append("        <th>六</th>\n");
        htmlBuilder.append("    </tr>\n");

        // days列表已经按日期排序
        int currentWeekday = 0;
        boolean firstRow = true;

        for (Day day : monthDays) {
            if (firstRow) {
                htmlBuilder.append("    <tr>\n");
                for (int i = 0; i < day.weekday; i++) {
                    htmlBuilder.append("        <td></td>\n");
                    currentWeekday++;
                }
                firstRow = false;
            }

            if (currentWeekday == 0) {
                htmlBuilder.append("    <tr>\n");
            }

            htmlBuilder.append("        <td");
            if (day.year == this.year && day.month == this.month && day.day == this.day) {
                htmlBuilder.append(" class=\"today\"");
            } else if (day.isOffDay) {
                htmlBuilder.append(" class=\"offday\"");
            }
            htmlBuilder.append(">");
            htmlBuilder.append(day.day);
            if (day.tag != null && !day.tag.isEmpty()) {
                htmlBuilder.append("<br><span class=\"tag\">").append(day.tag).append("</span>");
            }
            htmlBuilder.append("</td>\n");

            currentWeekday++;
            if (currentWeekday == 7) {
                htmlBuilder.append("    </tr>\n");
                currentWeekday = 0;
            }
        }

        if (currentWeekday != 0) {
            for (int i = currentWeekday; i < 7; i++) {
                htmlBuilder.append("        <td></td>\n");
            }
            htmlBuilder.append("    </tr>\n");
        }

        return htmlBuilder.toString();
    }

}