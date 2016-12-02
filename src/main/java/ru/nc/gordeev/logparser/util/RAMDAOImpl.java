package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RAMDAOImpl implements DAO {
    private static Map<String, LogFile> library = new ConcurrentHashMap<>();
    public void insert(LogFile file){library.put(file.getPath(),file);}
    public LogFile find(String key){
        return library.get(key);
    }
    public void delete(String key){
        library.remove(key);
    }
    public void update(String key, LogFile file){
        library.replace(key,file);
    }
    public int countLines(String key){ return library.get(key).getLogs().size(); }
    public int countFiles(){
        return library.size();
    }
    public void clear(){
        library.clear();
    }
    public void show(String key) {
        System.out.println(library.get(key));
    }
    public void showAll() {
        library.forEach((key,file)-> System.out.println(key));
    }
    public boolean contains(String key) {return library.containsKey(key);}
}
