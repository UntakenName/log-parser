package ru.nc.gordeev.logparser.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LogLine {
    public static final DateTimeFormatter LOG_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private DateTime date = new DateTime(); //the date-time of an instance creation by default
    private String mark;   //not sure what this part of a log stands for so declared it as String
    private String logLevel;
    private String classPath;
    private String message;

    public DateTime getDate() {
        DateTime taken = new DateTime(date);
        return taken;
    }

    public String getMark() {
        return mark;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getClassPath() {
        return classPath;
    }

    public String getMessage() {
        return message;
    }

    public LogLine() {
    }

    public LogLine(DateTime date,
            String mark,
            String logLevel,
            String classPath,
            String message) {
        this.date = date;
        this.mark = mark;
        this.logLevel = logLevel;
        this.classPath = classPath;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%1$s [%2$7s]%3$7s - %4$30s - %5$s",
                date.toString(LOG_TIME_FORMAT),
                mark,
                logLevel,
                classPath,
                message);
    }
}