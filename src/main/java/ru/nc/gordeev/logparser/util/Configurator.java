package ru.nc.gordeev.logparser.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Configurator {
    public static Pattern logFormat;
    public static DateTimeFormatter logTimeFormat;
    public static String lineToStringFormat;
    public static Properties configuration=new Properties();
    public static void getConfiguration(String path){
        try (InputStream input = new FileInputStream(path)) {
            configuration.load(input);
            configureFormat(configuration);
            configureStorage(configuration);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"SOMETHING WENT WRONG!",e);
            return;
        }
    }
    private static void configureFormat(Properties prop) {
        String localLogFormat=prop.getProperty("logFormat");
        String localLogTimeFormat=prop.getProperty("logTimeFormat");
        if (localLogTimeFormat!=null) {
            logTimeFormat=DateTimeFormat.forPattern(localLogTimeFormat);
        } else logTimeFormat=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
        if (localLogFormat!=null) {
            Map<Integer,LogLinePart> order=new TreeMap<>();
            for(LogLinePart part:LogLinePart.values()){
                int start = localLogFormat.indexOf(part.placeHolder);
                if(start>=0){
                    order.put(start,part);
                    part.specifyRange(start,localLogFormat.lastIndexOf(part.placeHolder));
                }
            }
            int[] i={1};
            order.forEach((num,part)->part.setRegExpGroup(i[0]++));
            StringBuilder pattern = new StringBuilder();
            for(LogLinePart part:LogLinePart.values()){
                if(part.range>0) {
                    for (i[0] = 0; i[0] < part.range; i[0]++) {
                        pattern.append(part.placeHolder);
                    }
                    if (localLogFormat.indexOf("*") - 1 != localLogFormat.indexOf(part.placeHolder)) {
                        localLogFormat = localLogFormat.replace(pattern, "%" + (part.ordinal() + 1) + "$" + part.range + "s");
                    } else {
                        localLogFormat = localLogFormat.replace(pattern.append('*'), "%" + (part.ordinal() + 1) + "$s");
                    }
                    pattern.delete(0, pattern.length());
                }
            }
            lineToStringFormat=localLogFormat;
            String regexp = localLogFormat
                    .replace("[","\\[")
                    .replace("]","\\]")
                    .replace("{","\\{")
                    .replace("}","\\}")
                    .replace("(","\\(")
                    .replace(")","\\)");
            for(LogLinePart part:LogLinePart.values()){
                if(part!=LogLinePart.DATE) {
                    regexp=regexp.replace("%"+(part.ordinal()+1)+"$"+part.range+"s","(.{"+part.range+"})?");
                } else {
                    String dateRegExp=localLogTimeFormat.replaceAll("\\w","\\\\d");
                    regexp=regexp.replace("%"+(part.ordinal()+1)+"$"+part.range+"s","("+dateRegExp+")?");
                }
            }
            logFormat=Pattern.compile(regexp.replaceAll("%\\d\\$s","(.*)?"));
        } else {
            logFormat=Pattern.compile("(\\d{4}-\\d{2}-\\d{2}( \\d{2}):\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
            lineToStringFormat="%1$s [%2$7s]%3$7s - %4$30s - %5$s";
        }
    }
    private static void configureStorage(Properties prop) {
        try {
            StorageType destinationStorage;
            String whereToStore = prop.getProperty("whereToStore");
            if (whereToStore!=null){
                destinationStorage = StorageType.valueOf(prop.getProperty("whereToStore").toUpperCase());
            } else  destinationStorage=StorageType.RAM;
            Class factoryClass = Class.forName(destinationStorage.DAOfactroyName);
            DataManager.setFactory((DAOFactory) factoryClass.newInstance());
            System.out.println(destinationStorage+" type of data storage has been applied!");
        } catch (IllegalArgumentException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"Can't storage where configured",e);
        } catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"No such DAOfactory as required",e);
        }
    }
    public static void getInitialConfigurations() {
        logFormat=Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
        logTimeFormat=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS");
        lineToStringFormat="%1$s [%2$7s]%3$7s - %4$30s - %5$s";
        DataManager.setFactory(new RAMDAOFactory());
        for(LogLinePart part:LogLinePart.values()){
            part.setRegExpGroup(part.ordinal()+1);
        }
    }
}
