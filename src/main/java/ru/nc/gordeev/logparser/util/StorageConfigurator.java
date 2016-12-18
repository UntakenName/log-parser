package ru.nc.gordeev.logparser.util;

import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.IDAO;
import ru.nc.gordeev.logparser.data.RAMDAOImpl;

import java.lang.reflect.Constructor;
import java.util.Properties;


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
        boolean storageHasBeenChanged = false;
        try {
            if (whereToStore!=null&&!(whereToStore.equals(whereStored))){
                appliedStorage = StorageType.valueOf(whereToStore.toUpperCase());
                Class factory = Class.forName(appliedStorage.getDAOFactoryName());
                Constructor constructor = factory.getConstructor(Properties.class);
                currentConfiguration.setDAOFactory((IDAOFactory) constructor.newInstance(properties));
                currentConfiguration.setProperty("whereToStore",whereToStore);
                storageHasBeenChanged=true;
            }
            if (workWith!=null&&(storageHasBeenChanged||!(workWith.equals(workingWith)))) {
                IDAO concreteDAO = currentConfiguration.getDAOFactory().getDAOImplementation(workWith);
                currentConfiguration.setDAO(concreteDAO);
                currentConfiguration.setProperty("workWith",whereToStore);
                System.out.println("Application now works with log"+workWith+"s");
            }
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(StorageConfigurator.class).warn("Can't storage where configured!",e);
        } catch (Exception e) {
            LoggerFactory.getLogger(StorageConfigurator.class).warn("No such DAO as required!",e);
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfiguration) {
        currentConfiguration.setProperty("whereToStore","ram");
        currentConfiguration.setDAO(new RAMDAOImpl());
    }
}
