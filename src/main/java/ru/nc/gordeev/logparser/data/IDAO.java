package ru.nc.gordeev.logparser.data;

import java.util.ArrayList;

/** DAO interface serves to weaken the coupling between the application functionality and applicable
 *  storage types. To make the application work with another type of storage, create DAO implementation
 *  of this storage and declare an element within StorageType enum.
 */

public interface IDAO {
    void insert(LogFile file);
    LogFile find(String path);
    void delete(String path);
    void update(String path, LogFile file);
    int countLines(String path);
    int countFiles();
    ArrayList<String> getContent ();
    boolean contains(String path);
    boolean connectionIsEstablished();
}
