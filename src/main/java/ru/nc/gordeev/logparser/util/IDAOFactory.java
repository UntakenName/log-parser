package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.IDAO;

public interface IDAOFactory {
    IDAO getDAOImplementation(String DAOType) throws Exception;
}
