package szu.dky.clockcalendar.service.todo;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import dev.webview.webview_java.bridge.JavascriptObject;
import dev.webview.webview_java.bridge.JavascriptFunction;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import szu.dky.clockcalendar.view.UI;
import szu.dky.clockcalendar.config.DataConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoAPI extends JavascriptObject {

    private List<Todo> todoList;
    private static final String DEFAULT_PATH = "/todo.json";

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
        return new TodoAPI();
    }

    public TodoAPI() {
        todoList = loadTodos(DataConfig.DATA_DIR + DEFAULT_PATH);
    }

    @GetMapping("/todo/getTodoList")
    @JavascriptFunction
    public String getTodoList() {
        StringBuilder htmlBuilder = new StringBuilder();

        for (int i = 0; i < todoList.size(); i++) {
            Todo todo = todoList.get(i);
            htmlBuilder.append("<div class=\"todo-item\">\n")
                       .append("  <span>").append(todo.task).append("</span>\n")
                       .append("  <span class=\"date\">").append(todo.getDate()).append("</span>\n")
                       .append("  <button class=\"button\" onclick=\"doneTodo(").append(i).append(")\">完成</button>\n")
                       .append("</div>\n");
        }

        return htmlBuilder.toString();
    }

    @PostMapping("/todo/doneTodo")
    @JavascriptFunction
    public String doneTodo(@RequestBody int index) {
        try {
            todoList.remove(index);
        } catch (Exception e) {
        }
        saveTodo(DataConfig.DATA_DIR + DEFAULT_PATH);
        return getTodoList();
    }

    @PostMapping("/todo/addTodo")
    @JavascriptFunction
    // public String addTodo(@RequestBody String task, @RequestBody String deadlineStr) {
    public String addTodo(@RequestBody Todo todo) {
        try {
            // todoList.add(new Todo().task(task).deadline(deadlineStr));
            todoList.add(todo);
        } catch (Exception e) {
            e.printStackTrace();
            UI.getInstance().eval("alert('参数非法')");
        }
        saveTodo(DataConfig.DATA_DIR + DEFAULT_PATH);
        return getTodoList();
    }

    public void saveTodo(String path) {
        String json = JSON.toJSONString(todoList);
        try {
            Files.write(Path.of(path), json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
    }
    
}