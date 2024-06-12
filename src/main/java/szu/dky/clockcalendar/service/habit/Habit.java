package szu.dky.clockcalendar.service.habit;

import java.util.Date;
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
    public Date startAt;
}