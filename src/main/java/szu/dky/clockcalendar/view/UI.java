package szu.dky.clockcalendar.view;

import dev.webview.webview_java.Webview;
import dev.webview.webview_java.bridge.WebviewBridge;

import szu.dky.clockcalendar.util.*;

public class UI extends Webview {

    public volatile static UI instance;

    public WebviewBridge bridge;

    private UI() {
        super(true);
    }

    // double-checked locking
    public static UI getInstance() {
        if (instance == null) {
            synchronized (UI.class) {
                if (instance == null) {
                    instance = new UI();
                    instance.applyDefaultConfig();
                }
            }
        }
        return instance;
    }

    private void applyDefaultConfig() {
        this.bridge = new WebviewBridge(this);
        this.bind("setDarkAppearance", (arguments) -> {
            this.setDarkAppearance(arguments.contains("true"));
            return null;
        });

        this.setTitle("Clock Calendar");
        this.setSize(800, 600);

        try {
            this.setHTML(HTMLLoader.loadFromResource("/demo.html"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}