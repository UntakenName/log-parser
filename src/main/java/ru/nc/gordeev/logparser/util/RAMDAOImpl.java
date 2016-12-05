package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.RAMStorage;

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
    public void clear(){
        RAMStorage.getInstance().getLibrary().clear();
    }
    public void show(String key) {
        System.out.println(RAMStorage.getInstance().getLibrary().get(key));
    }
    public void showAll() {
        RAMStorage.getInstance().getLibrary().forEach((key,file)-> System.out.println(key));
    }
    public boolean contains(String key) {return RAMStorage.getInstance().getLibrary().containsKey(key);}
    public boolean connectionIsEstablished() {return true;}
}
