package szu.dky.clockcalendar.util;

import java.awt.Toolkit;

public class Beeper {

    public static void ringBell() {
        Toolkit.getDefaultToolkit().beep();
    }

}