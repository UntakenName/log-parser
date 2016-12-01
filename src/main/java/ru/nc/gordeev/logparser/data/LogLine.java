package ru.nc.gordeev.logparser.data;

import org.joda.time.DateTime;
import ru.nc.gordeev.logparser.util.LogLinePart;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import static ru.nc.gordeev.logparser.util.Configurator.*;

public class LogLine {
    private DateTime date = new DateTime(); //the date-time of an instance creation by default
    private String mark;   //not sure what this part of a log stands for so declared it as String
    private String logLevel;
    private String classPath;
    private String message;

    public <T> T getAParsedPart(LogLinePart part){
        Class line = LogLine.class;
        try {
            Field field =line.getDeclaredField(part.fieldName);
            System.out.println(field);
            if(part != LogLinePart.DATE) {
                return (T) field.get(this);
            } else return (T) new DateTime(field.get(this));
        } catch (NoSuchFieldException|IllegalAccessException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"Can't manage operation");
            return null; }
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
        return String.format(lineToStringFormat,
                date.toString(logTimeFormat),
                mark,
                logLevel,
                classPath,
                message);
    }
}