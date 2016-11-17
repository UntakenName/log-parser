package ru.nc.gordeev.logparser;

import org.joda.time.DateTime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.regex.*;

import static ru.nc.gordeev.logparser.LogLine.LOG_TIME_FORMAT;

/**
 * Created by Sovereign on 12.11.2016.
 */
public class LogDecoder {
    public static void main(String[] args) {    }
    static final Pattern LOG=Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})? \\[(.{7})?\\](.{7})? - (.{30})? - (.*)?");
    public static LogFile decodeLogFile(String path){
        String line;
        ArrayList<String> content=new ArrayList<>();
        final int[] i = {0};
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();
            while (line!=null) {
                content.add(i[0]++,line);
                line=reader.readLine();
            }
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"SOMETHING WENT WRONG!",e);
            return new LogFile(path,new ArrayList<>());
        }
        ArrayList<LogLine> logs=new ArrayList<>();
        i[0] =0;
        content.forEach((logLine)-> {
            Matcher matcher = LOG.matcher(logLine);
            if(matcher.find()){
                DateTime date = matcher.group(1) !=null ? LOG_TIME_FORMAT.parseDateTime(matcher.group(1)) : new DateTime();
                String mark = matcher.group(2)!=null ? matcher.group(2).trim() : null;
                String logLevel = matcher.group(3)!=null ? matcher.group(3).trim() : null;
                String classPath = matcher.group(4)!=null ? matcher.group(4).trim() : null;
                String message = matcher.group(5)!=null ? matcher.group(5).trim() : null;
                logs.add(i[0]++,new LogLine(date,mark,logLevel,classPath,message));
            }
        });
        return new LogFile(path,logs);
    }
}
