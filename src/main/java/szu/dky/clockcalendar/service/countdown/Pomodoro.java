package szu.dky.clockcalendar.service.countdown;

import java.util.Date;
import com.alibaba.fastjson2.annotation.JSONField;

class Pomodoro {

    @JSONField
    private Date date;

    @JSONField
    private int count;

}