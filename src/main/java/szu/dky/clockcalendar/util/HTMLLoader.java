package szu.dky.clockcalendar.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import szu.dky.clockcalendar.App;

public class HTMLLoader {

    public static String load(String htmlPath) throws IOException {
        return Files.readString(Path.of(htmlPath));
    }

    public static String loadFromResource(String htmlPath) throws IOException {
        InputStream in = App.class.getResourceAsStream(htmlPath);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder html = new StringBuilder("");
        String line = null;
        while ((line = br.readLine()) != null) {
            html.append(line);
            html.append("\n");
        }
        br.close();
        isr.close();
        in.close();
        return html.toString();
    }

}