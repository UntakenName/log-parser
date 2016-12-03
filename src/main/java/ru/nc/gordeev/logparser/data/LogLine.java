package ru.nc.gordeev.logparser.data;

import org.joda.time.DateTime;
import static ru.nc.gordeev.logparser.data.Configurations.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogLine)) return false;

        LogLine line = (LogLine) o;

        if (!date.equals(line.date)) return false;
        if (mark != null ? !mark.equals(line.mark) : line.mark != null) return false;
        if (logLevel != null ? !logLevel.equals(line.logLevel) : line.logLevel != null) return false;
        if (classPath != null ? !classPath.equals(line.classPath) : line.classPath != null) return false;
        return message != null ? message.equals(line.message) : line.message == null;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        result = 31 * result + (logLevel != null ? logLevel.hashCode() : 0);
        result = 31 * result + (classPath != null ? classPath.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}