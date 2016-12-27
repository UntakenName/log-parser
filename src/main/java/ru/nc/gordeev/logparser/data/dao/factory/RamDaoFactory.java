package ru.nc.gordeev.logparser.data.dao.factory;

import ru.nc.gordeev.logparser.config.Configurations;
import ru.nc.gordeev.logparser.data.dao.FileRamDaoImpl;
import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.data.dao.LineRamDaoImpl;


public class RamDaoFactory implements IDaoFactory {

    public RamDaoFactory(Configurations crutch) {
        System.out.println("RAM type of data storage has been applied!");
    }

    @Override
    public IDao getDaoImplementation(String DaoType) {
        switch (DaoType.toLowerCase()) {
            case "line":
                return new LineRamDaoImpl();
            case "file":
                return new FileRamDaoImpl();
        }
        return null;
    }
}
