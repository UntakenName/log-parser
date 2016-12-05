package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.StorageType;

public class RAMDAOFactory implements IDAOFactory{
    @Override
    public IDAO getDAOImplementation(String DAOType) throws
            ClassNotFoundException, InstantiationException,IllegalAccessException{
        switch (DAOType.toLowerCase()) {
            case "line":
                DAOType= StorageType.RAM.getLineDAOImplName();
                break;
            case "file":
                DAOType=StorageType.RAM.getFileDAOImplName();
                break;
        }
        Class DAOClass = Class.forName(DAOType);
        return (IDAO) DAOClass.newInstance();
    }
}
