package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

public interface DAO {
    public void insert(LogFile file);
    public LogFile find(String path);
    public void delete(String path);
    public void update(String path, LogFile file);
    public int countLines(String path);
    public int countFiles();
    public void clear();
    public void show(String path);
    public void showAll();
    public boolean contains(String path);
}
