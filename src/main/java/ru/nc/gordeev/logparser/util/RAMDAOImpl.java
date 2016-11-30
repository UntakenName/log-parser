package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.RAMStorage;

public class RAMDAOImpl implements DAO {
    RAMStorage storage = RAMStorage.getInstance();
    public void insert(LogFile file){storage.lib.put(file.getPath(),file);}
    public LogFile find(String key){
        return storage.lib.get(key);
    }
    public void delete(String key){
        storage.lib.remove(key);
    }
    public void update(String key, LogFile file){
        storage.lib.replace(key,file);
    }
    public int countLines(String key){ return storage.lib.get(key).getLogs().size(); }
    public int countFiles(){
        return storage.lib.size();
    }
    public void clear(){
        storage.lib.clear();
    }
    public void show(String key) {
        System.out.println(storage.lib.get(key));
    }
    public void showAll() {
        storage.lib.forEach((key,file)->
                System.out.println(key));
    }
    public boolean contains(String key) {return storage.lib.containsKey(key);}
}
