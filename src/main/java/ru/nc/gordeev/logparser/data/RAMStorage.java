package ru.nc.gordeev.logparser.data;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RAMStorage {
    private static RAMStorage INSTANCE =null;
    public Map<String, LogFile> lib = new ConcurrentHashMap<>();
    public static synchronized RAMStorage getInstance() {
        if(INSTANCE==null) {INSTANCE=new RAMStorage();}
        return INSTANCE;
    }
    private RAMStorage(){}
}