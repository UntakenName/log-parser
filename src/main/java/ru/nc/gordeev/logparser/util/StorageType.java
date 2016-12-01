package ru.nc.gordeev.logparser.util;

public enum StorageType {
    RAM("ru.nc.gordeev.logparser.util.RAMDAOFactory");
    String DAOfactroyName;
    StorageType(String path) {
        DAOfactroyName=path;
    }
}
