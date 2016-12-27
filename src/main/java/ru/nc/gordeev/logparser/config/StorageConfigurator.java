package ru.nc.gordeev.logparser.config;

import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.dao.FileRamDaoImpl;
import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.util.StorageType;

import java.util.Properties;


    /** Class serves to configure type of data storage according to SRP.
    *  Class that contains configurations to work with should have static
    *  methods void setDAO(DAO) and void setProperty(String,String)
    */

public class StorageConfigurator implements IConfigurator {

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
                currentConfiguration.setDaoFactory(appliedStorage.getDaoFactory(currentConfiguration));
                currentConfiguration.setProperty("whereToStore",whereToStore);
                storageHasBeenChanged=true;
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(StorageConfigurator.class).warn("Can't storage where configured!", e);
        }
        if (workWith!=null&&(storageHasBeenChanged||!(workWith.equals(workingWith)))) {
            IDao concreteDao = currentConfiguration.getDaoFactory().getDaoImplementation(workWith);
            if (concreteDao!=null) {
                currentConfiguration.setDao(concreteDao);
                currentConfiguration.setProperty("workWith",whereToStore);
                System.out.println("Application now works with log"+workWith+"s");
            } else System.out.println("No such DAO as required!");
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfiguration) {
        currentConfiguration.setProperty("whereToStore","ram");
        currentConfiguration.setDao(new FileRamDaoImpl());
    }
}
