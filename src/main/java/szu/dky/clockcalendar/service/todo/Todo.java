package szu.dky.clockcalendar.service.todo;

import java.util.Date;
import com.alibaba.fastjson2.annotation.JSONField;

public class Todo {
    
    @JSONField
    public String task;

    @JSONField(format = "yyyy-MM-dd")
    public Date deadline;

}