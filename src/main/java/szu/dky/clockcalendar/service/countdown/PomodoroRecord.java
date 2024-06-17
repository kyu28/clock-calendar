package szu.dky.clockcalendar.service.countdown;

import com.alibaba.fastjson2.annotation.JSONField;

public class PomodoroRecord {
    @JSONField
    public String date;

    @JSONField
    public int count;
}