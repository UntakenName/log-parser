package ru.nc.gordeev.logparser.config;

import com.zaxxer.hikari.HikariDataSource;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.nc.gordeev.logparser.data.dao.FileRamDaoImpl;
import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.data.dao.factory.IDaoFactory;
import ru.nc.gordeev.logparser.data.dao.factory.RamDaoFactory;


import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

public class Configurations {

    private Pattern logFormat;
    private DateTimeFormatter logTimeFormat;
    private String lineToStringFormat;
    private IDaoFactory concreteFactory;
    private IDao concreteDao;
    private Properties properties = new Properties();
    private HikariDataSource connectionSource;

    public Configurations() {
        Locale.setDefault(Locale.US);
        setLogFormat("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
        setLogTimeFormat("yyyy-MM-dd HH:mm:ss,SSS");
        setLineToStringFormat("%1$s [%2$7s]%3$7s - %4$30s - %5$s");
        setDaoFactory(new RamDaoFactory(null));
        setDao(new FileRamDaoImpl());
        setProperty("logFormat", "ddddddddddddddddddddddd [mmmmmmm]lllllll - oooooooooooooooooooooooooooooo - t*");
        setProperty("logTimeFormat", "yyyy-MM-dd HH:mm:ss,SSS");
        setProperty("whereToStore", "ram");
        setProperty("workWith", "file");
        setProperty("url","jdbc:oracle:thin:@localhost:1521:XE");
        setProperty("user","system");
        setProperty("password","1234");
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

    public IDaoFactory getDaoFactory() {
        return concreteFactory;
    }

    public IDao getDao() {
        return concreteDao;
    }

    public Properties getProperties() {
        return (Properties) properties.clone();
    }

    public HikariDataSource getConnectionSource() {return connectionSource;}

    public void setLogFormat(String format) {
        logFormat = Pattern.compile(format);
    }

    public void setLogTimeFormat(String pattern) {
        logTimeFormat = DateTimeFormat.forPattern(pattern);
    }

    public void setLineToStringFormat(String format) {
        lineToStringFormat = format;
    }

    public void setDaoFactory(IDaoFactory factory) {
        concreteFactory = factory;
    }

    public void setDao(IDao DaoImpl) {
        concreteDao = DaoImpl;
    }

    public void setProperty(String prop, String value) {
        properties.setProperty(prop, value);
    }

    public void setConnectionSource(HikariDataSource source) {connectionSource=source;}

    @Override
    public String toString() {
        return properties.toString();
    }
}
