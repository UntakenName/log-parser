package ru.nc.gordeev.logparser.util;

import java.util.Properties;
import static ru.nc.gordeev.logparser.data.Configurations.*;

    /** Class serves to configure DateTime format according to SRP.
     *  Class that contains configurations to work with should have static
     *  methods void setLogTimeFormat(String) and void setProperty(String,String)
     */
public class DateConfigurator implements Configurator {
    @Override
    public void setConfiguration(Properties properties) {
        String logTimeFormat=properties.getProperty("logTimeFormat");
        if (logTimeFormat!=null) {
            getCurrentConfigurations().setLogTimeFormat(logTimeFormat);
            getCurrentConfigurations().setProperty("logTimeFormat",logTimeFormat);
        } else setInitialConfiguration();
    }

    @Override
    public void setInitialConfiguration() {
        getCurrentConfigurations().setLogTimeFormat("yyyy-MM-dd HH:mm:ss,SSS");
        getCurrentConfigurations().setProperty("logTimeFormat","yyyy-MM-dd HH:mm:ss,SSS");
    }
}
