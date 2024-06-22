package szu.dky.clockcalendar;

import co.casterlabs.commons.async.AsyncTask;

import dev.webview.webview_java.bridge.JavascriptFunction;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptValue;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.service.*;
import szu.dky.clockcalendar.config.DataConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        if (!Files.exists(Paths.get(DataConfig.DATA_DIR))) {
            try {
                Files.createDirectories(Paths.get(DataConfig.DATA_DIR));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        UI ui = UI.getInstance();
        ServiceGateway gateway = ServiceGateway.getInstance();
        
        // ui.bridge.defineObject("Todo", gateway.getService(ServiceName.TODO));
        // ui.bridge.defineObject("Datetime", gateway.getService(ServiceName.DATETIME));
        // ui.bridge.defineObject("Countdown", gateway.getService(ServiceName.COUNTDOWN));
        // ui.bridge.defineObject("Habit", gateway.getService(ServiceName.HABIT));
        // ui.bridge.defineObject("Router", gateway.getService(ServiceName.ROUTER));
        new Thread(() -> {
            SpringApplication.run(App.class, args);
        }).start();

        ui.run();
        ui.close();
        gateway.shutdownAll();
        System.exit(0);
    }

}