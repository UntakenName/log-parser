package ru.nc.gordeev.logparser.data.dao;

import ru.nc.gordeev.logparser.data.RamStorage;
import ru.nc.gordeev.logparser.data.entity.LogFile;

import java.util.*;

public class FileRamDaoImpl implements IDao {
    public void insert(LogFile file){
        RamStorage.getInstance().getLibrary().put(file.getPath(),file);}
    public LogFile find(String key){
        return RamStorage.getInstance().getLibrary().get(key);
    }
    public void delete(String key){
        RamStorage.getInstance().getLibrary().remove(key);
    }
    public void update(String key, LogFile file){
        RamStorage.getInstance().getLibrary().replace(key,file);
    }
    public int countLines(String key){ return RamStorage.getInstance().getLibrary().get(key).getLogs().size(); }
    public int countFiles(){
        return RamStorage.getInstance().getLibrary().size();
    }
    public ArrayList<String> getContent() {
        return new ArrayList<>(RamStorage.getInstance().getLibrary().keySet());
    }
    public boolean contains(String key) {return RamStorage.getInstance().getLibrary().containsKey(key);}
    public boolean connectionIsEstablished() {return true;}
}
