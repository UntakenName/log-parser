package ru.nc.gordeev.logparser;



import java.util.ArrayList;

/**
 * Created by Sovereign on 10.11.2016.
 */
public class LogFile {
    public static void main(String[] args) {}
    private String path = "Unknown";
    private ArrayList<LogLine> logs = new ArrayList<>();

    public String getPath() {return path;}

    public ArrayList<LogLine> getLogs() {
        ArrayList<LogLine> taken = new ArrayList<>();
        logs.forEach((logLine)->{taken.add(logs.indexOf(logLine),logLine);});
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
        logs.forEach((logLine)->{file.append("\n" + logLine.toString());});
        return file.toString();
    }
}
