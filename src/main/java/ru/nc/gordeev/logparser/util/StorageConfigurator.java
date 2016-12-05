package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.Configurations;
import ru.nc.gordeev.logparser.data.StorageType;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

    /** Class serves to configure type of data storage according to SRP.
    *  Class that contains configurations to work with should have static
    *  methods void setDAO(DAO) and void setProperty(String,String)
    */

public class StorageConfigurator implements IConfigurator{

    @Override
    public void setConfiguration(Configurations currentConfiguration, Properties properties) {
        StorageType appliedStorage;
        String whereToStore = properties.getProperty("whereToStore");
        String whereStored = currentConfiguration.getProperties().getProperty("whereToStore");
        String workWith = properties.getProperty("workWith");
        String workingWith = currentConfiguration.getProperties().getProperty("workWith");
        try {
            if (whereToStore!=null&&!(whereToStore.equals(whereStored))){
                appliedStorage = StorageType.valueOf(whereToStore.toUpperCase());
                Class factory = Class.forName(appliedStorage.getDAOFactoryName());
                currentConfiguration.setDAOFactory((IDAOFactory) factory.newInstance());
                currentConfiguration.setProperty("whereToStore",whereToStore);
                System.out.println(appliedStorage+" type of data storage has been applied!");
            }
            if (workWith!=null&&!(workWith.equals(workingWith))) {
                IDAO concreteDAO = currentConfiguration.getDAOFactory().getDAOImplementation(workWith);
                currentConfiguration.setDAO(concreteDAO);
                currentConfiguration.setProperty("workWith",whereToStore);
                System.out.println("Application now works with log"+workWith+"s");
            }
        } catch (IllegalArgumentException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"Can't storage where configured",e);
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"No such DAO as required",e);
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfiguration) {
        currentConfiguration.setProperty("whereToStore","ram");
        currentConfiguration.setDAO(new RAMDAOImpl());
    }
}
