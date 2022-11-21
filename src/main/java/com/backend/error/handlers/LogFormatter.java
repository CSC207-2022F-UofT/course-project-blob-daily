package com.backend.error.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    // ANSI escape code
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[0;91m";
    public static final String ANSI_YELLOW = "\u001b[33;1m";
    public static final String ANSI_GREY = "\u001B[37m";
    public static final String ANSI_WHITE = "\u001B[37m";


    /**
     * Format log message to appeal to developers when debugging/testing
     * @param record the log record to be formatted
     * @return the formatted log message
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(ANSI_GREY);

        builder.append("[");
        builder.append(calcDate(record.getMillis()));
        builder.append("]");

        builder.append(" [");
        builder.append(record.getSourceClassName());
        builder.append("]");

        builder.append(" [");
        switch (record.getLevel().getName()) {
            case "WARNING" : {
                builder.append(ANSI_YELLOW);
                break;
            }
            case "SEVERE" : {
                builder.append(ANSI_RED);
                break;
            }
            default : builder.append(ANSI_GREY);
        }

        builder.append(record.getLevel().getName());
        builder.append(ANSI_GREY);
        builder.append("]");

        builder.append(ANSI_GREY);
        builder.append(" - ");

        switch (record.getLevel().getName()) {
            case "WARNING" : builder.append(ANSI_WHITE);
            case "SEVERE" : builder.append(ANSI_RED);
            default : builder.append(ANSI_GREY);
        }

        builder.append(record.getMessage());

        Object[] params = record.getParameters();

        if (params != null)
        {
            builder.append("/t");
            for (int i = 0; i < params.length; i++)
            {
                builder.append(params[i]);
                if (i < params.length - 1)
                    builder.append(", ");
            }
        }

        builder.append(ANSI_RESET);
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Calculate the date in the format of year-month-day hour:minute:second from the given LONG parameter
     * @param milliseconds of type long, milliseconds representation of date as a long
     * @return the date in the above stated format
     */
    private String calcDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultDate = new Date(milliseconds);
        return dateFormat.format(resultDate);
    }
}
