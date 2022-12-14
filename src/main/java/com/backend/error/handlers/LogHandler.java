package com.backend.error.handlers;


import com.backend.entities.criteria.conditions.IConditionHandler;
import com.backend.usecases.IErrorHandler;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LogHandler implements IConditionHandler, IErrorHandler {
    // Variables
    private static final Logger LOGGER = Logger.getLogger( LogHandler.class.getName() );

    private static final HashMap<String, Level> levelConverter = new HashMap<>();

    public static boolean DEPRECATED = true;

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

    /**
     * Log a message to the console with the given configuration (parameters)
     * @param message of type String, message to be displayed
     * @param level of type String, level of message to be displayed
     */
    public static void logMessage(String message, String level) {
        LOGGER.log(levelConverter.get(level), message);
    }

    /**
     * Log an info message to the console with the given parameter
     * @param message of type String, message to be displayed
     */
    public void logInfo(String message) {
        logMessage(message, "INFO");
    }

    /**
     * Log a warning message to the console with the given parameter
     * @param message of type String, message to be displayed
     */
    @Override
    public void logWarning(String message) {
        logMessage(message, "WARNING");
    }

    /**
     * Log an error message to the console with the given parameter
     * @param e of type Exception, exception with information to be displayed (message, stackTrace, etc)
     */
    @Override
    public void logError(Exception e) {
        if (!DEPRECATED) {
            if (e.getMessage() != null) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            } else {
                LOGGER.log(Level.SEVERE, e.getClass().getName(), e);
            }

            e.printStackTrace();
        } else {
            this.logInfo(e.getMessage());
        }
    }

    /**
     * Log an error message to the console with the given parameter and package a response entity with the given information
     * @param exception of type Exception, exception with information to be displayed (message, stackTrace, etc)
     * @param status of type HttpStatus, status of the REST response to be generated
     * @return a ResponseEntity with the associated exception message and status code
     */
    @Override
    public ResponseEntity<Object> logError(Exception exception, HttpStatus status) {
        this.logError(exception);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Error", exception.getClass().getName());
        jsonObject.put("Message", exception.getMessage());


        return new ResponseEntity<>(jsonObject, status);

    }
}