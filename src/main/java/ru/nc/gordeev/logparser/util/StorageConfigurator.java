package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.StorageType;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static ru.nc.gordeev.logparser.data.Configurations.*;

    /** Class serves to configure type of data storage according to SRP.
    *  Class that contains configurations to work with should have static
    *  methods void setDAO(DAO) and void setProperty(String,String)
    */

public class StorageConfigurator implements Configurator{
    @Override
    public void setConfiguration(Properties properties) {
        try {
            StorageType appliedStorage;
            String whereToStore = properties.getProperty("whereToStore");
            if (whereToStore!=null){
                appliedStorage = StorageType.valueOf(properties.getProperty("whereToStore").toUpperCase());
            } else  {
                setInitialConfiguration();
                return;
            }
            getCurrentConfigurations().setDAO(new DAOFactory().getDAOImplementation(appliedStorage));
            getCurrentConfigurations().setProperty("whereToStore",whereToStore);
            System.out.println(appliedStorage+" type of data storage has been applied!");
        } catch (IllegalArgumentException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"Can't storage where configured",e);
            setInitialConfiguration();
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"No such DAO as required",e);
            setInitialConfiguration();
        }
    }

    @Override
    public void setInitialConfiguration() {
        getCurrentConfigurations().setProperty("whereToStore","ram");
        getCurrentConfigurations().setDAO(new RAMDAOImpl());
    }
}
