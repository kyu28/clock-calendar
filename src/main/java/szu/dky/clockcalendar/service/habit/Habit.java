package szu.dky.clockcalendar.service.habit;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;

public class Habit {

    @JSONField
    public String name;

    @JSONType
    public enum Freq {
        DAILY,
        WEEKLY,
        MONTHLY;
    }

    @JSONField
    public Freq freq;

    @JSONField(format = "yyyy-MM-dd")
    public Date[] checkins;

    @JSONField
    public int chain;

    @JSONField(format = "yyyy-MM-dd")
    public Date startDate;

    public Habit() {
        this.checkins = new Date[0];
        this.chain = 0;
    }
    
    public Habit name(String name) {
        this.name = name;
        return this;
    }

    public Habit freq(String freq) {
        switch (freq) {
        case "DAILY":
            this.freq = Freq.DAILY;
            break;
        case "WEEKLY":
            this.freq = Freq.WEEKLY;
            break;
        case "MONTHLY":
            this.freq = Freq.MONTHLY;
            break;
        }
        return this;
    }

    public Habit startDate(String startDate) throws ParseException {
        this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        return this;
    }

    public String getStartDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(startDate);
    }
}