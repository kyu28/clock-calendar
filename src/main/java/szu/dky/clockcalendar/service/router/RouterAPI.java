package szu.dky.clockcalendar.service.router;

import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.util.*;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouterAPI extends JavascriptObject {

    public static JavascriptObject getService() {
        return new RouterAPI();
    }

    @PostMapping("/router/to")
    @JavascriptFunction
    public String to(@RequestBody String htmlPath) {
        // UI ui = UI.getInstance();
        // try {
            // ui.setHTML(HTMLLoader.loadFromResource(htmlPath));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        return "http://localhost:8080" + htmlPath;
    }

    public void shutdown() {
    }

}