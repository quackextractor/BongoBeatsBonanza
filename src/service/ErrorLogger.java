package service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

    private static final String LOG_FILE = "error_log.txt";

    public static void logStackTrace(Exception e) {
        try {
            FileWriter fileWriter = new FileWriter(LOG_FILE, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());

            printWriter.println("Timestamp: " + timestamp);
            e.printStackTrace(printWriter);

            printWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
