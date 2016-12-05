package ru.nc.gordeev.logparser.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RAMStorage {
    private static RAMStorage instance = null;
    private Map<String, LogFile> library = new ConcurrentHashMap<>();

    private RAMStorage(){}

    public static RAMStorage getInstance() {
        if (instance==null) {
            instance=new RAMStorage();
        }
        return instance;
    }

    public Map<String,LogFile> getLibrary() {
        return library;
    }


}
