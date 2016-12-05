package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.Configurations;
import ru.nc.gordeev.logparser.data.LogLinePart;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/** Class serves to configure format of parsed logs according to SRP.
 *  Class that contains configurations to work with should have static
 *  methods void setLineToStringFormat(String), void setLogFormat(String)
 *  and void setProperty(String,String)
 */

public class ParsingConfigurator implements IConfigurator {
    @Override
    public void setConfiguration(Configurations currentConfiguration, Properties properties) {
        String logFormat=properties.getProperty("logFormat");
        String logTimeFormat=properties.getProperty("logTimeFormat");
        if (logFormat!=null&&!(logFormat.equals(currentConfiguration.getProperties().getProperty("logFormat")))) {
            currentConfiguration.setProperty("logFormat",logFormat);

            //Regex groups order determination for each logline part in order of appearance in the "logFormat" property
            Map<Integer,LogLinePart> order=new TreeMap<>();
            for(LogLinePart part:LogLinePart.values()){
                int start = logFormat.indexOf(part.getPlaceHolder());
                if(start>=0){
                    order.put(start,part);
                    part.specifyRange(start,logFormat.lastIndexOf(part.getPlaceHolder()));
                }
            }
            int[] i={1};
            order.forEach((num,part)->part.setRegExpGroup(i[0]++));

            //LogLine toString() format determination in accordance with the "logFormat" property
            StringBuilder pattern = new StringBuilder();
            for(LogLinePart part:LogLinePart.values()){
                if(part.getRange()>0) {
                    for (i[0] = 0; i[0] < part.getRange(); i[0]++) {
                        pattern.append(part.getPlaceHolder());
                    }
                    if (logFormat.indexOf("*") - 1 != logFormat.indexOf(part.getPlaceHolder())) {
                        logFormat = logFormat.replace(pattern, "%" + (part.ordinal() + 1) + "$" + part.getRange() + "s");
                    } else {
                        logFormat = logFormat.replace(pattern.append('*'), "%" + (part.ordinal() + 1) + "$s");
                    }
                    pattern.delete(0, pattern.length());
                }
            }
            currentConfiguration.setLineToStringFormat(logFormat);

            //It's easier to build a regex for a logline parsing using LogLine toString() format
            String regExp = logFormat
                    .replace("[","\\[")
                    .replace("]","\\]")
                    .replace("{","\\{")
                    .replace("}","\\}")
                    .replace("(","\\(")
                    .replace(")","\\)");
            for(LogLinePart part:LogLinePart.values()){
                if(part!=LogLinePart.DATE) {
                    regExp=regExp.replace("%"+(part.ordinal()+1)+"$"+part.getRange()+"s","(.{"+part.getRange()+"})?");
                } else {
                    String dateRegExp=logTimeFormat.replaceAll("\\w","\\\\d");
                    regExp=regExp.replace("%"+(part.ordinal()+1)+"$"+part.getRange()+"s","("+dateRegExp+")?");
                }
            }
            currentConfiguration.setLogFormat(regExp.replaceAll("%\\d\\$s","(.*)?"));
        }
    }

    @Override
    public void setInitialConfiguration(Configurations currentConfiguration) {
        currentConfiguration.setLogFormat("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
        currentConfiguration.setLineToStringFormat("%1$s [%2$7s]%3$7s - %4$30s - %5$s");
        currentConfiguration.setProperty("logFormat","ddddddddddddddddddddddd [mmmmmmm]lllllll - oooooooooooooooooooooooooooooo - t*");
        for(LogLinePart part:LogLinePart.values()){
            part.setRegExpGroup(part.ordinal()+1);
        }
    }
}
