package szu.dky.clockcalendar.service.datetime;

import java.util.Objects;
import com.alibaba.fastjson2.annotation.JSONField;

class DateObject {
    @JSONField
    public String date;

    @JSONField
    public String tag;

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateObject other = (DateObject)o;
        return this.date.equals(other.date);
    }

}
