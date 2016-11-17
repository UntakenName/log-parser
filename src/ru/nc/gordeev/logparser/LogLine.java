package ru.nc.gordeev.logparser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Sovereign on 10.11.2016.
 */
public class LogLine {
    public static void main(String[] args) {}
    static final DateTimeFormatter LOG_TIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private DateTime date = new DateTime(); //если данные о времени не найдены, то присваивается дата создания объекта
    private String mark;   //пока не понял, за что отвечает эта часть лога, т.ч. определил стригом
    private String logLevel;
    private String classPath;
    private String message;

    DateTime getDate() {
        DateTime taken = new DateTime(date);
        return taken;
    }

    String getMark() {
        return mark;
    }

    String getLogLevel() {
        return logLevel;
    }

    String getClassPath() {
        return classPath;
    }

    String getMessage() {
        return message;
    }

    LogLine() {
    }

    LogLine(DateTime date,
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