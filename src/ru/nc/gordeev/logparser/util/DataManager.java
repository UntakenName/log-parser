package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

public class DataManager {
    public static StorageFactory factory=new RAMStorageFactory();
    public static void getRAMStorage() {
        if(!(factory instanceof RAMStorageFactory)) {
            factory=new RAMStorageFactory();
        }
    }
    public static void insert(LogFile file) {
        factory.getStorage().insert(file);
    }
    public static LogFile find(String path){
        return factory.getStorage().find(path);
    }
    public static void delete(String path) {
        factory.getStorage().delete(path);
    }
    public static void update(String path, LogFile file) {
        factory.getStorage().update(path,file);
    }
    public static void countLines(String path) {
        System.out.println("The number of lines in the " +path+" is: "+factory.getStorage().countLines(path));
    }
    public static void countFiles() {
        System.out.println("The number of files in the library is: " + factory.getStorage().countFiles());
    }
    public static void clear() {
        factory.getStorage().clear();
    }
    public static void show(String path){
        factory.getStorage().show(path);
    }
    public static void showAll() {
        factory.getStorage().showAll();
    }

}
