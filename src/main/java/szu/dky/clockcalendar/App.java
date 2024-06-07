package szu.dky.clockcalendar;

import co.casterlabs.commons.async.AsyncTask;

import dev.webview.webview_java.bridge.JavascriptFunction;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptValue;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.service.*;

import java.net.URL;

public class App {

    public static void main(String[] args) {
        UI ui = UI.getInstance();

        ServiceGateway gateway = ServiceGateway.getInstance();
        ui.bridge.defineObject("Beep", gateway.getService(ServiceName.BEEP));
        ui.bridge.defineObject("Router", gateway.getService(ServiceName.ROUTER));
        ui.bridge.defineObject("Test", new TestObject());

        ui.run();
        ui.close();
    }

    public static class TestObject extends JavascriptObject {

        @JavascriptValue(allowSet = false, watchForMutate = true)
        public long nanoTime = -1;
        {
            AsyncTask.create(() -> {
                try {
                    while (true) {
                        this.nanoTime = System.nanoTime();
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ignored) {}
            });
        }

    }

}