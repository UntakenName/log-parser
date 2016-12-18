package ru.nc.gordeev.logparser.data;

import java.util.*;

public class RAMDAOImpl implements IDAO {
    public void insert(LogFile file){RAMStorage.getInstance().getLibrary().put(file.getPath(),file);}
    public LogFile find(String key){
        return RAMStorage.getInstance().getLibrary().get(key);
    }
    public void delete(String key){
        RAMStorage.getInstance().getLibrary().remove(key);
    }
    public void update(String key, LogFile file){
        RAMStorage.getInstance().getLibrary().replace(key,file);
    }
    public int countLines(String key){ return RAMStorage.getInstance().getLibrary().get(key).getLogs().size(); }
    public int countFiles(){
        return RAMStorage.getInstance().getLibrary().size();
    }
    public ArrayList<String> getContent() {
        return new ArrayList<>(RAMStorage.getInstance().getLibrary().keySet());
    }
    public boolean contains(String key) {return RAMStorage.getInstance().getLibrary().containsKey(key);}
    public boolean connectionIsEstablished() {return true;}
}
