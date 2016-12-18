package ru.nc.gordeev.logparser.util;



import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.*;


public class ConfigurationManager {

    private static Configurations currentConfigurations=new Configurations();

    //ver 0.0.7: configurators initial capacity equals to the highly probable number of applied configurators
    private ArrayList<IConfigurator> configurators=new ArrayList<>(3);

    public static Configurations getCurrentConfigurations() {
        return currentConfigurations;
    }

    //This constructor serves to manage distinct configuration
    public ConfigurationManager(IConfigurator... appliedConfigurators) {
        configurators.ensureCapacity(appliedConfigurators.length);
        configurators.addAll(Arrays.asList(appliedConfigurators));
    }

    public void setConfigurations(String path){
        Properties newConfigurations=new Properties();
        try (InputStream input = new FileInputStream(path)) {
            newConfigurations.load(input);
        } catch (IOException e) {
            LoggerFactory.getLogger(ConfigurationManager.class).warn("Can't set configurations!",e);
            return;
        }
        configurators.forEach(conf->conf.setConfiguration(currentConfigurations, newConfigurations));
    }

    public void setInitialConfigurations() {
        configurators.forEach(conf->conf.setInitialConfiguration(currentConfigurations));
        System.out.println("Initial configurations have been applied!");
    }
}
