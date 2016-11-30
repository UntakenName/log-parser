package ru.nc.gordeev.logparser.util;

public class RAMDAOFactory implements DAOFactory{
    public DAO getDAOImplementation() {
        return new RAMDAOImpl();
    }
}
