package szu.dky.clockcalendar.service.router;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.util.*;
import java.io.IOException;

public class Main extends JavascriptObject {

    public static JavascriptObject getService() {
        return new Main();
    }

    @JavascriptFunction
    public void to(String htmlPath) {
        UI ui = UI.getInstance();
        try {
            ui.setHTML(HTMLLoader.loadFromResource(htmlPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}