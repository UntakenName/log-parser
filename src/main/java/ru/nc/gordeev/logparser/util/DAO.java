package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

public interface DAO {
    void insert(LogFile file);
    LogFile find(String path);
    void delete(String path);
    void update(String path, LogFile file);
    int countLines(String path);
    int countFiles();
    void clear();
    void show(String path);
    void showAll();
    boolean contains(String path);
}
