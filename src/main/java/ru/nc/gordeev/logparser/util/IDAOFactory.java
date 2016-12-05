package ru.nc.gordeev.logparser.util;

public interface IDAOFactory {
    IDAO getDAOImplementation(String DAOType) throws
            ClassNotFoundException, InstantiationException,IllegalAccessException;
}
