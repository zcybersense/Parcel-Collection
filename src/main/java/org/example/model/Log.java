package org.example.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    // Static instance for Singleton pattern
    private static Log instance;
    private PrintWriter writer;

    // Private constructor to initialize the PrintWriter to write logs to a file
    private Log() {
        try {
            // Open file for appending logs (it creates the file if it doesn't exist)
            writer = new PrintWriter(new FileWriter("application.log", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get the Singleton instance of Logger
    public static Log getInstance() {
        if (instance == null) {
            synchronized (Log.class) {
                if (instance == null) {
                    instance = new Log();
                }
            }
        }
        return instance;
    }

    // Log info messages
    public void info(String message) {
        log("INFO", message);
    }

    // Log warning messages
    public void warning(String message) {
        log("WARNING", message);
    }

    // Log error messages
    public void error(String message) {
        log("ERROR", message);
    }

    // Core logging method to handle different log levels
    private void log(String level, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        writer.println("[" + timestamp + "] [" + level + "] " + message);
        writer.flush();  // Ensure the log is written immediately to the file
    }

    // Close the writer (for cleanup, usually at the end of the program)
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
