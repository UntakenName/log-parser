package ru.nc.gordeev.logparser.data;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.nc.gordeev.logparser.util.DAO;
import ru.nc.gordeev.logparser.util.RAMDAOImpl;

import java.util.Properties;
import java.util.regex.Pattern;

public class Configurations {
    private static Configurations currentConfigurations = new Configurations();
    private Pattern logFormat=Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
    private DateTimeFormatter logTimeFormat=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private String lineToStringFormat="%1$s [%2$7s]%3$7s - %4$30s - %5$s";
    private DAO concreteDAO= new RAMDAOImpl();
    private Properties properties = new Properties();
    {
        setProperty("logFormat","ddddddddddddddddddddddd [mmmmmmm]lllllll - oooooooooooooooooooooooooooooo - t*");
        setProperty("logTimeFormat","yyyy-MM-dd HH:mm:ss,SSS");
        setProperty("whereToStore","ram");
    }

    public static Configurations getCurrentConfigurations() {
        return currentConfigurations;
    }

    public Pattern getLogFormat() {
        return logFormat;
    }

    public DateTimeFormatter getLogTimeFormat() {
        return logTimeFormat;
    }

    public String getLineToStringFormat() {
        return lineToStringFormat;
    }

    public DAO getDAO() {
        return concreteDAO;
    }

    public Properties getProperties(){
        return (Properties) properties.clone();
    }

    public void setLogFormat(String format) {
        logFormat = Pattern.compile(format);
    }

    public void setLogTimeFormat(String pattern) {
        logTimeFormat = DateTimeFormat.forPattern(pattern);
    }

    public void setLineToStringFormat(String format) {
        lineToStringFormat = format;
    }

    public void setDAO(DAO DAOImpl) {
        concreteDAO = DAOImpl;
    }

    public void setProperty(String prop,String value) {
        properties.setProperty(prop,value);
    }
}
