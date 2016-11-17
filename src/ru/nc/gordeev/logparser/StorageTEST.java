package ru.nc.gordeev.logparser;


import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Sovereign on 13.11.2016.
 */
public class StorageTEST {
    public static void main(String[] args) {    }
    public static final StorageTEST INSTANCE =new StorageTEST();
    private Map<String, LogFile> RAMStrorage = new HashMap<>();
    private StorageTEST(){}
    public void insert(LogFile file){
        RAMStrorage.put(file.getPath(),file);
    }
    public LogFile find(String key){
        return RAMStrorage.get(key);
    }
    public void delete(String key){
        RAMStrorage.remove(key);
    }
    public void update(String key, LogFile file){
        RAMStrorage.replace(key,file);
    }
    public int countLines(String key){
        return RAMStrorage.get(key).getLogs().size();
    }
    public int countFiles(){
        return RAMStrorage.size();
    }
    public void clear(){
        RAMStrorage.clear();
    }
    public void show(String key) {
        System.out.println(RAMStrorage.get(key));
    }
    public void showAll() {
        RAMStrorage.forEach((key,file)->
                System.out.println(key));
    }

}