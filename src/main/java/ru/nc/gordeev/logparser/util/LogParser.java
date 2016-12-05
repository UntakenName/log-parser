package ru.nc.gordeev.logparser.util;
import org.joda.time.DateTime;
import ru.nc.gordeev.logparser.data.Configurations;
import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.LogLine;
import ru.nc.gordeev.logparser.data.LogLinePart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import java.util.regex.*;

public class LogParser {

    private DataManager manager;

    private Configurations config;

    LogParser(DataManager dataManager,Configurations configurations) {
        manager=dataManager;
        config=configurations;
    }

    public void parseFile(String path){
        ArrayList<String> content = obtainContent(path);
        LogFile parsedLog = parse(path,content);
        if (parsedLog.getLogs().size()>0) {
            manager.insert(parsedLog);
        } else System.out.println("No specified log format lines were detected.");
    }

    private ArrayList<String> obtainContent(String path) {
        String line;
        ArrayList<String> content=new ArrayList<>();
        final int[] i = {0};
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            line = reader.readLine();
            while (line!=null) {
                content.add(i[0]++,line);
                line=reader.readLine();
            }
            return content;
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.WARNING,"SOMETHING WENT WRONG!",e);
            return new ArrayList<>();
        }
    }

    private LogFile parse(String path, Collection<String> content) {
        ArrayList<LogLine> logs=new ArrayList<>(content.size());
        final int[] i = {0};
        content.forEach((contentLine)-> {
            Matcher matcher = config.getLogFormat().matcher(contentLine);
            if(matcher.find()){
                Map<LogLinePart,Object> partContent = new HashMap<>(5);
                for(LogLinePart part: LogLinePart.values()){
                    if(part!=LogLinePart.DATE){
                        if(part.getRegExpGroup()>0 && matcher.group(part.getRegExpGroup())!=null) {
                            partContent.put(part,matcher.group(part.getRegExpGroup()).trim());
                        } else partContent.put(part,null);
                    } else if(part.getRegExpGroup()>0 && matcher.group(part.getRegExpGroup())!=null) {
                        partContent.put(part,config.getLogTimeFormat()
                                        .parseDateTime(matcher.group(part.getRegExpGroup())));
                    } else partContent.put(part,new DateTime());
                }
                logs.add(i[0]++,new LogLine(
                        (DateTime)partContent.get(LogLinePart.DATE),
                        (String)partContent.get(LogLinePart.MARK),
                        (String)partContent.get(LogLinePart.LOG_LEVEL),
                        (String)partContent.get(LogLinePart.CLASS_PATH),
                        (String)partContent.get(LogLinePart.MESSAGE)));
            }
        });
        return new LogFile(path, logs);
    }
}
