package ru.nc.gordeev.logparser.data.entity;

import org.joda.time.DateTime;
import static ru.nc.gordeev.logparser.config.ConfigurationManager.*;

public class LogLine {
    private DateTime date = new DateTime(); //the date-time of an instance creation by default
    private String mark;   //not sure what this part of a log stands for so declared it as String
    private String logLevel;
    private String classPath;
    private String message;

    public DateTime getDate() {
        return new DateTime(date);
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
        return String.format(getCurrentConfigurations().getLineToStringFormat(),
                date.toString(getCurrentConfigurations().getLogTimeFormat()),
                mark,
                logLevel,
                classPath,
                message);
    }
}