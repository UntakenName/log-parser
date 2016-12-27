package ru.nc.gordeev.logparser.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.dao.factory.DbDaoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DbConnectionConfigurator implements IConfigurator {
    @Override
    public void setConfiguration(Configurations currentConfigurations, Properties newConfigurations) {
        String[] credentials = {"url","user","password"};
        ArrayList<String> trueCredentials = new ArrayList<>(3);
        for (String credential:credentials) {
            String localCred = newConfigurations.getProperty(credential);
            if (localCred!=null) {
                trueCredentials.add(localCred);
            } else trueCredentials.add(currentConfigurations.getProperties().getProperty(credential));
        }

        HikariDataSource connectionPool=new HikariDataSource();

        connectionPool.setJdbcUrl(trueCredentials.get(0));
        connectionPool.setUsername(trueCredentials.get(1));
        connectionPool.setPassword(trueCredentials.get(2));

        try (Connection connection = connectionPool.getConnection()) {
            currentConfigurations.setConnectionSource(connectionPool);
            currentConfigurations.setProperty("url",trueCredentials.get(0));
            currentConfigurations.setProperty("user",trueCredentials.get(1));
            currentConfigurations.setProperty("password",trueCredentials.get(2));
        } catch (SQLException e) {
            LoggerFactory.getLogger(DbDaoFactory.class).error("Can't connect to the database!",e);
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfigurations) {
        currentConfigurations.setConnectionSource(null);
    }
}
