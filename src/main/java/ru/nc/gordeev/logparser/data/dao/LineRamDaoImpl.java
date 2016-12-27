package ru.nc.gordeev.logparser.data.dao;




import org.joda.time.*;
import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.RamStorage;
import ru.nc.gordeev.logparser.data.entity.LogFile;
import ru.nc.gordeev.logparser.data.entity.LogLine;


import java.util.ArrayList;
import java.util.Comparator;



public class LineRamDaoImpl extends AbstractLineDao {

    @Override
    protected ArrayList<LogLine> findLines(Interval interval) {
        ArrayList<LogLine> logs = new ArrayList<>(1000);
        for(LogFile file: RamStorage.getInstance().getLibrary().values()) {
            ArrayList<LogLine> localLogs = file.getLogs();
            for(LogLine line: localLogs) {
                if (interval.contains(line.getDate())) {
                    logs.add(line);
                }
            }
        }
        logs.trimToSize();
        return logs;
    }

    @Override
    public void insert(LogFile file) {
        if (!RamStorage.getInstance().getLibrary().containsKey(file.getPath())) {
            RamStorage.getInstance().getLibrary().put(file.getPath(),file);
        }
    }

    @Override
    public void delete(String query) {
        Interval interval = toInterval(query);
        ArrayList<LogLine> logs = new ArrayList<>(1000);
        for(LogFile file:RamStorage.getInstance().getLibrary().values()) {
            ArrayList<LogLine> localLogs = file.getLogs();
            localLogs.removeIf(line -> interval.contains(line.getDate()));
            update(file.getPath(),new LogFile(file.getPath(),localLogs));
        }
    }

    @Override
    public void update(String path, LogFile file) {
        RamStorage.getInstance().getLibrary().replace(path,file);
    }

    @Override
    public int countLines(String query) {
            Interval interval = toInterval(query);
            return findLines(interval).size();
    }

    @Override
    public int countFiles() {
        return RamStorage.getInstance().getLibrary().size();
    }

    @Override
    public ArrayList<String> getContent() {
        DateTime first=null;
        DateTime last=null;
        for(LogFile file:RamStorage.getInstance().getLibrary().values()) {
            ArrayList<LogLine> localLogs = file.getLogs();
            localLogs.sort(Comparator.comparing(LogLine::getDate));
            if (first==null||localLogs.get(0).getDate().compareTo(first)<0) {
                first=localLogs.get(0).getDate();
            }
            if (last==null||localLogs.get(localLogs.size()-1).getDate().compareTo(last)>0) {
                last=localLogs.get(localLogs.size()-1).getDate();
            }
        }
        ArrayList<String> output = new ArrayList<>(2);
        output.add(first.toString());
        output.add(last.toString());
        return output;
    }

    @Override
    public boolean contains(String query) {
        try {
            Interval interval = toInterval(query);
            for (LogFile file : RamStorage.getInstance().getLibrary().values()) {
                ArrayList<LogLine> localLogs = file.getLogs();
                for (LogLine line : localLogs) {
                    if (interval.contains(line.getDate())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(LineRamDaoImpl.class).warn("Can't parse DateTime out of the query!",e);
            return false;
        }
    }

    @Override
    public boolean connectionIsEstablished() {
        return true;
    }
}
