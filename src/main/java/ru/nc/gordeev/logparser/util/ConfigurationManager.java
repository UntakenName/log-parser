package ru.nc.gordeev.logparser.util;



import ru.nc.gordeev.logparser.data.Configurations;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationManager {

    private Properties configuration=new Properties();

    //ver 0.0.7: configurators initial capacity equals to the highly probable number of applied configurators
    private ArrayList<Configurator> configurators=new ArrayList<>(3);

    public void setConfigurations(String path){
        try (InputStream input = new FileInputStream(path)) {
            configuration.load(input);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"SOMETHING WENT WRONG!",e);
            setInitialConfigurations();
            return;
        }
        configurators.forEach(conf->conf.setConfiguration(configuration));
    }

    public void setInitialConfigurations() {
        configurators.forEach(Configurator::setInitialConfiguration);
    }

    //ver 0.0.7: current property display should be a ConfMan function, but more elegant solution just hadn't come
    public Properties getCurrentConfigurations() {
        return Configurations.getCurrentConfigurations().getProperties();
    }

    public ConfigurationManager(Configurator... appliedConfigurators) {
        configurators.ensureCapacity(appliedConfigurators.length);
        configurators.addAll(Arrays.asList(appliedConfigurators));
    }
}
