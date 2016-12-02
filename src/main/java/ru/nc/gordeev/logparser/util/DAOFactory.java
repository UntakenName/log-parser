package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.StorageType;

public class DAOFactory {
    public DAO getDAOImplementation(StorageType storage)
            throws ClassNotFoundException, InstantiationException,IllegalAccessException{
        Class DAOClass = Class.forName(storage.DAOImplementationName);
        return (DAO) DAOClass.newInstance();
    }
}
