package ru.nc.gordeev.logparser.util;


import ru.nc.gordeev.logparser.config.Configurations;
import ru.nc.gordeev.logparser.data.dao.factory.DbDaoFactory;
import ru.nc.gordeev.logparser.data.dao.factory.IDaoFactory;
import ru.nc.gordeev.logparser.data.dao.factory.RamDaoFactory;

import java.lang.reflect.Constructor;

/** Enum serves to weaken the coupling between the application functionality and applicable storage types.
 *  To make the application work with another type of storage, create DAO implementation of this storage and
 *  declare an element within this enum. Value of the element should be expressed exactly as in the Property
 *  file. The element should contain information about the DAO Factory class.
 */

public enum StorageType {
    RAM(RamDaoFactory.class),
    DB(DbDaoFactory.class);
    private Class<IDaoFactory> DaoFactoryName;

    StorageType(Class clazz) {
        DaoFactoryName=clazz;
    }

    public IDaoFactory getDaoFactory(Configurations configurations) throws Exception{
        Constructor<IDaoFactory> constructor = DaoFactoryName.getConstructor(Configurations.class);
        return constructor.newInstance(configurations);
    }
}
