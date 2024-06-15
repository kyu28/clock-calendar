package szu.dky.clockcalendar.service.todo;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson2.annotation.JSONField;

public class Todo {
    
    @JSONField
    public String task;

    @JSONField(format = "yyyy-MM-dd")
    public Date deadline;

    public Todo task(String task) {
        this.task = task;
        return this;
    }

    public Todo deadline(String dateStr) throws ParseException {
        this.deadline = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        return this;
    }

    public String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.deadline);
    }

}