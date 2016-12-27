package ru.nc.gordeev.logparser.data.dao.factory;

import ru.nc.gordeev.logparser.data.dao.IDao;

public interface IDaoFactory {
    IDao getDaoImplementation(String DaoType);
}
