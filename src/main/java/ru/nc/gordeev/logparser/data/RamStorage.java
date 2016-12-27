package ru.nc.gordeev.logparser.data;

import ru.nc.gordeev.logparser.data.entity.LogFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RamStorage {
    private static RamStorage instance = null;
    private Map<String, LogFile> library = new ConcurrentHashMap<>();

    private RamStorage(){}

    public static RamStorage getInstance() {
        if (instance==null) {
            instance=new RamStorage();
        }
        return instance;
    }

    public Map<String,LogFile> getLibrary() {
        return library;
    }


}
