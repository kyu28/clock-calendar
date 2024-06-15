package szu.dky.clockcalendar.service.todo;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;

public class Main extends JavascriptObject {

    private List<Todo> todoList;
    private static final String DEFAULT_PATH = "../mock/todo.json";

    public List<Todo> loadTodos(String path) {
        try {
            String serializedList = Files.readString(Path.of(path));
            return JSON.parseArray(serializedList, Todo.class, JSONReader.Feature.SupportSmartMatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Todo>();
    }

    public static JavascriptObject getService() {
        return new Main();
    }

    public Main() {
        todoList = loadTodos(DEFAULT_PATH);
        System.out.println(getTodoList());
    }

    @JavascriptFunction
    public String getTodoList() {
        StringBuilder htmlBuilder = new StringBuilder();

        for (Todo todo : todoList) {
            htmlBuilder.append("<div class=\"todo-item\">\n")
                       .append("  <span>").append(todo.task).append("</span>\n")
                       .append("  <span class=\"date\">").append(todo.getDate()).append("</span>\n")
                       .append("  <button class=\"button\">Done</button>\n")
                       .append("</div>\n");
        }

        return htmlBuilder.toString();
    }

    @JavascriptFunction
    public String doneTodo(int index) {
        try {
            todoList.remove(index);
        } catch (Exception e) {
            return "";
        }
        return getTodoList();
    }

    @JavascriptFunction
    public String addTodo(String task, String deadlineStr) {
        try {
            todoList.add(new Todo().task(task).deadline(deadlineStr));
        } catch (Exception e) {
            return "";
        }
        return getTodoList();
    }

}