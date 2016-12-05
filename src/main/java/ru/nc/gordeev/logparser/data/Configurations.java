package ru.nc.gordeev.logparser.data;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.nc.gordeev.logparser.util.IDAO;
import ru.nc.gordeev.logparser.util.IDAOFactory;
import ru.nc.gordeev.logparser.util.RAMDAOFactory;
import ru.nc.gordeev.logparser.util.RAMDAOImpl;

import java.util.Properties;
import java.util.regex.Pattern;

public class Configurations {

    private Pattern logFormat;
    private DateTimeFormatter logTimeFormat;
    private String lineToStringFormat;
    private IDAOFactory concreteFactory;
    private IDAO concreteDAO;
    private Properties properties = new Properties();

    public Configurations() {
        setLogFormat("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
        setLogTimeFormat("yyyy-MM-dd HH:mm:ss,SSS");
        setLineToStringFormat("%1$s [%2$7s]%3$7s - %4$30s - %5$s");
        setDAOFactory(new RAMDAOFactory());
        setDAO(new RAMDAOImpl());
        setProperty("logFormat", "ddddddddddddddddddddddd [mmmmmmm]lllllll - oooooooooooooooooooooooooooooo - t*");
        setProperty("logTimeFormat", "yyyy-MM-dd HH:mm:ss,SSS");
        setProperty("whereToStore", "ram");
        setProperty("workWith", "file");
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

    public IDAOFactory getDAOFactory() {
        return concreteFactory;
    }

    public IDAO getDAO() {
        return concreteDAO;
    }

    public Properties getProperties() {
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

    public void setDAOFactory(IDAOFactory factory) {
        concreteFactory = factory;
    }

    public void setDAO(IDAO DAOImpl) {
        concreteDAO = DAOImpl;
    }

    public void setProperty(String prop, String value) {
        properties.setProperty(prop, value);
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
