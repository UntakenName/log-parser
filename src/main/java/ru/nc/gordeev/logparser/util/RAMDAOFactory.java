package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.IDAO;

import java.util.Properties;

public class RAMDAOFactory implements IDAOFactory{

    public RAMDAOFactory(Properties crutch) {}

    public RAMDAOFactory() {
        System.out.println("RAM type of data storage has been applied!");
    }

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
