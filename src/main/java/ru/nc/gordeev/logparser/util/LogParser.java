package ru.nc.gordeev.logparser.util;
import org.joda.time.DateTime;
import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.LogLine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import java.util.regex.*;
import static ru.nc.gordeev.logparser.util.Configurator.*;

public class LogParser {
    public static void parseFile(String path){
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
            return;
        }
        ArrayList<LogLine> logs=new ArrayList<>(content.size());
        i[0] =0;
        content.forEach((contentLine)-> {
            Matcher matcher = logFormat.matcher(contentLine);
            if(matcher.find()){
                Map<LogLinePart,Object> partContent = new HashMap<LogLinePart, Object>(5);
                for(LogLinePart part: LogLinePart.values()){
                    if(part!=LogLinePart.DATE){
                        if(part.regExpGroup>0 && matcher.group(part.regExpGroup)!=null) {
                             partContent.put(part,matcher.group(part.regExpGroup).trim());
                        } else partContent.put(part,null);
                    } else if(part.regExpGroup>0 && matcher.group(part.regExpGroup)!=null) {
                        partContent.put(part,logTimeFormat.parseDateTime(matcher.group(part.regExpGroup)));
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
        if (logs.size()>0) {
            if (!DataManager.contains(path)) {
                DataManager.insert(new LogFile(path, logs));
                System.out.println(path + " has been put in the library.");
            } else {
                DataManager.update(path,new LogFile(path,logs));
                System.out.println(path + " has been updated.");
            }
        } else System.out.println("No specified log format lines were detected.");

    }
}
