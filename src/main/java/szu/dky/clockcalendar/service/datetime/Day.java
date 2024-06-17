package szu.dky.clockcalendar.service.datetime;

import com.alibaba.fastjson2.annotation.JSONField;

public class Day {

    @JSONField
    public int year;

    @JSONField
    public int month;

    @JSONField
    public int day;

    @JSONField
    public byte weekday;

    @JSONField
    public boolean isOffDay;

    @JSONField
    public String tag;

    public Day() {
        tag = "";
    }

    public Day year(int year) {
        this.year = year;
        return this;
    }

    public Day month(int month) {
        this.month = month;
        return this;
    }

    public Day day(int day) {
        this.day = day;
        return this;
    }

    public Day weekday(byte weekday) {
        this.weekday = weekday;
        return this;
    }

    public Day isOffDay(boolean isOffDay) {
        this.isOffDay = isOffDay;
        return this;
    }

    public Day tag(String tag) {
        this.tag = tag;
        return this;
    }

}