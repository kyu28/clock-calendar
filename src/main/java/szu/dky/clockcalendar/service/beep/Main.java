package szu.dky.clockcalendar.service.beep;

import dev.webview.webview_java.bridge.JavascriptFunction;
import dev.webview.webview_java.bridge.JavascriptObject;
import java.awt.Toolkit;

public class Main extends JavascriptObject {

    public static JavascriptObject getService() {
        return new Main();
    }

    @JavascriptFunction
    public void ringBell() {
        Toolkit.getDefaultToolkit().beep();
    }

}