package ru.nc.gordeev.logparser.config;

import java.util.Properties;

    /** Class serves to configure DateTime format according to SRP.
     *  Class that contains configurations to work with should have static
     *  methods void setLogTimeFormat(String) and void setProperty(String,String)
     */
public class DateConfigurator implements IConfigurator {
    @Override
    public void setConfiguration(Configurations currentConfigurations, Properties properties) {
        String logTimeFormat=properties.getProperty("logTimeFormat");
        if (logTimeFormat!=null&&!(logTimeFormat.equals(currentConfigurations.getProperties().getProperty("logTimeFormat")))) {
            currentConfigurations.setLogTimeFormat(logTimeFormat);
            currentConfigurations.setProperty("logTimeFormat",logTimeFormat);
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfigurations) {
        currentConfigurations.setLogTimeFormat("yyyy-MM-dd HH:mm:ss,SSS");
        currentConfigurations.setProperty("logTimeFormat","yyyy-MM-dd HH:mm:ss,SSS");
    }
}
