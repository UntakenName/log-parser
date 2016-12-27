package ru.nc.gordeev.logparser.config;

import java.util.Properties;

    /** ConfigurationManager uses Configurator implementations to configure application properties.
     *  To add a property, declare it within a .property file, make a Configurator implementation
     *  which configures this property and hand it over to ConfigMans constructor along side with
     *  the other implementations.
     */

public interface IConfigurator {
    void setConfiguration(Configurations currentConfigurations, Properties newConfigurations);
    void setInitialConfiguration(Configurations currentConfigurations);
}
