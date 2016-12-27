package ru.nc.gordeev.logparser.data.dao.factory;


import com.zaxxer.hikari.HikariDataSource;
import ru.nc.gordeev.logparser.config.Configurations;
import ru.nc.gordeev.logparser.data.dao.FileDbDaoImpl;
import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.data.dao.LineDbDaoImpl;

import static ru.nc.gordeev.logparser.config.ConfigurationManager.*;


public class DbDaoFactory implements IDaoFactory {
    private HikariDataSource connectionPool;

    public DbDaoFactory(Configurations config) {
        connectionPool=config.getConnectionSource();
        System.out.println("DataBase type of data storage has been applied!");
    }

    @Override
    public IDao getDaoImplementation(String DaoType) {
        switch (DaoType.toLowerCase()) {
            case "line":
                return new LineDbDaoImpl(connectionPool);
            case "file":
                return new FileDbDaoImpl(connectionPool);
        }
       return null;
    }

    @Override
    protected void finalize() throws Throwable{
        connectionPool.close();
        super.finalize();
    }
}
