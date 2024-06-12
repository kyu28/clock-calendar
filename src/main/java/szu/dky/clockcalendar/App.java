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
        ui.bridge.defineObject("Router", gateway.getService(ServiceName.ROUTER));

        ui.run();
        ui.close();
    }

}