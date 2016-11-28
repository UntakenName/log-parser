package ru.nc.gordeev.logparser.util;

public class RAMStorageFactory implements StorageFactory{
    public IStorage getStorage() {
        return new IRAMStorage();
    }
}
