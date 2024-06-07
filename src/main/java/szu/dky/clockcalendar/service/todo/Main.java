package szu.dky.clockcalendar.service.todo;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import dev.webview.webview_java.bridge.JavascriptObject;

public class Main extends JavascriptObject {

    private static List<Todo> todoList = new ArrayList<Todo>();
    private static final String DEFAULT_PATH = "./todo.json";

    public void load(String path) {
        todoList = Files.readString(Path.of(htmlPath));
    }

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
        load(DEFAULT_PATH);
    }

    public String getTodoList() {
        return todoList;
    }

}