package ru.nc.gordeev.logparser.data;

import java.util.ArrayList;

public class LogFile {
    private String path = "Unknown";
    private ArrayList<LogLine> logs = new ArrayList<>();

    public String getPath() {return path;}

    public ArrayList<LogLine> getLogs() {
        ArrayList<LogLine> taken = new ArrayList<>();
        logs.forEach((logLine)->taken.add(logs.indexOf(logLine),logLine));
        return taken;
    }

    public LogFile() {
    }

    public LogFile(String path, ArrayList<LogLine> logs) {
        this.path = path;
        this.logs = logs;
    }

    @Override
    public String toString() {
        StringBuilder file = new StringBuilder(path);
        logs.forEach((logLine)->
            file.append("\n").append(logLine.toString())
        );
        return file.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogFile)) return false;

        LogFile logFile = (LogFile) o;

        return logs != null ? logs.equals(logFile.logs) : logFile.logs == null;
    }

    @Override
    public int hashCode() {
        return logs != null ? logs.hashCode() : 0;
    }
}
