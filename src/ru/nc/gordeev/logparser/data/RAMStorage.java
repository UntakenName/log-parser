package ru.nc.gordeev.logparser.data;


import java.util.HashMap;
import java.util.Map;

public class RAMStorage {
    private static RAMStorage INSTANCE =null;
    public Map<String, LogFile> lib = new HashMap<>();
    public static synchronized RAMStorage getInstance() {
        if(INSTANCE==null) {INSTANCE=new RAMStorage();}
        return INSTANCE;
    }
    private RAMStorage(){}
}