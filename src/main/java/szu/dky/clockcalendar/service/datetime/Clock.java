package szu.dky.clockcalendar.service.datetime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class Clock {

    private ScheduledExecutorService executorService;
    private static int hour;
    private static int minute;
    private static int second;

    public Clock() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String tokens[] = time.format(formatter).split(":");
        hour = Integer.parseInt(tokens[0]);
        minute = Integer.parseInt(tokens[1]);
        second = Integer.parseInt(tokens[2]);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> tick(), 0, 1, TimeUnit.SECONDS);
    }

    public String getTime() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void setTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public void syncTime(String ntpServer) {
        try {
            NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(10000);
            client.open();
            InetAddress hostAddr = InetAddress.getByName(ntpServer);
            TimeInfo info = client.getTime(hostAddr);
            long serverTime = info.getMessage().getTransmitTimeStamp().getTime();

            // Convert server time to hours, minutes, and seconds
            long currentTimeMillis = serverTime;
            long totalSeconds = currentTimeMillis / 1000;
            long currentSecond = totalSeconds % 60;
            long totalMinutes = totalSeconds / 60;
            long currentMinute = totalMinutes % 60;
            long totalHours = totalMinutes / 60;
            long currentHour = (totalHours % 24) + 8;

            setTime((int)currentHour, (int)currentMinute, (int)currentSecond);

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tick() {
        second++;
        if (second == 60) {
            second = 0;
            minute++;
        }
        if (minute == 60) {
            minute = 0;
            hour++;
        }
        if (hour == 24) {
            hour = 0;
            DatetimeAPI.calendar.dateForward();
        }
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

}