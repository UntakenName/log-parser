package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;
import static ru.nc.gordeev.logparser.data.Configurations.*;

public class DataManager {

    public void insert(LogFile file) {
        getCurrentConfigurations().getDAO().insert(file);
    }
    public LogFile find(String path){
        if (getCurrentConfigurations().getDAO().contains(path)) {
            return getCurrentConfigurations().getDAO().find(path);
        } else return null;
    }

    public void delete(String path) {
        if (getCurrentConfigurations().getDAO().contains(path)) {
            getCurrentConfigurations().getDAO().delete(path);
        }
    }

    public void update(String path, LogFile file) {
        if (getCurrentConfigurations().getDAO().contains(path)) {
            getCurrentConfigurations().getDAO().update(path,file);
        }
    }

    public void countLines(String path) {
        if (getCurrentConfigurations().getDAO().contains(path)) {
            System.out.println("The number of lines in the " + path + " is: "
                    + getCurrentConfigurations().getDAO().countLines(path));
        }
    }

    public void countFiles() {
        System.out.println("The number of files in the library is: " +
                getCurrentConfigurations().getDAO().countFiles());
    }

    public void clear(){getCurrentConfigurations().getDAO().clear();
    }

    public void show(String path){
        if (getCurrentConfigurations().getDAO().contains(path)) {
            getCurrentConfigurations().getDAO().show(path);
        }
    }

    public void showAll() {
        getCurrentConfigurations().getDAO().showAll();
    }

    public boolean contains(String path) {
        return getCurrentConfigurations().getDAO().contains(path);
    }
}
