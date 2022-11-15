package com.questpets.error.handlers;



import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHandler {
    private static final Logger LOGGER = Logger.getLogger( LogHandler.class.getName() );

    private static final HashMap<String, Level> levelConverter = new HashMap<>();

    public static boolean DEPRECATED = false;

    static {
        levelConverter.put("INFO", Level.INFO);
        levelConverter.put("ALL", Level.ALL);
        levelConverter.put("SEVERE", Level.SEVERE);
        levelConverter.put("WARNING", Level.WARNING);
        levelConverter.put("FINE", Level.FINE);
        levelConverter.put("FINER", Level.FINER);
        levelConverter.put("FINEST", Level.FINEST);

        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();

        Formatter formatter = new LogFormatter();
        handler.setFormatter(formatter);

        LOGGER.addHandler(handler);
    }

    public static void logMessage(String message, String level) {
        LOGGER.log(levelConverter.get(level), message);
    }

    public static void logInfo(String message) {
        logMessage(message, "INFO");
    }

    public static void logWarning(String message) {
        logMessage(message, "WARNING");
    }

    public static void logSevere(String message) {
        logMessage(message, "SEVERE");
    }

    public static void logError(Exception e) {
        if (!DEPRECATED) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        } else {
            logInfo(e.getMessage());
        }
    }

    public static void main(String[] args) {
        logWarning("hey this may be important");
    }
}
