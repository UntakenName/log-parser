package ru.nc.gordeev.logparser.data;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.nc.gordeev.logparser.util.DAO;

import java.util.Properties;
import java.util.regex.Pattern;

public class Configurations {
    private static Configurations currentConfigurations = new Configurations();
    private Pattern logFormat;
    private DateTimeFormatter logTimeFormat;
    private String lineToStringFormat;
    private DAO concreteDAO;
    private Properties properties = new Properties();

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
