package ru.nc.gordeev.logparser.data;


import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.data.entity.LogFile;

import java.util.ArrayList;

import static ru.nc.gordeev.logparser.config.ConfigurationManager.*;

public class DataManager{

    private IDao concreteDao;

    public DataManager() {
        concreteDao=getCurrentConfigurations().getDao();
    }

    public void insert(LogFile file) {
        if (!concreteDao.contains(file.getPath())) {
            concreteDao.insert(file);
            System.out.println(file.getPath() + " has been put in the library.");
        } else System.out.println("File with the same path has been already parsed to the library.\nChange the path or Update the file. ");
    }
    public LogFile find(String query){
        if (concreteDao.contains(query)) {
            return concreteDao.find(query);
        } else return null;
    }

    public void delete(String query) {
        if (concreteDao.contains(query)) {
            concreteDao.delete(query);
        }
    }

    public void update(String path, LogFile file) {
        if (concreteDao.contains(path)) {
            concreteDao.update(path,file);
            System.out.println(path + " has been updated.");
        } else System.out.println("No such file on the library");
    }

    public void countLines(String query) {
        if (concreteDao.contains(query)) {
            System.out.println("The number of lines in the " + query + " is: "
                    + concreteDao.countLines(query));
        }
    }

    public void countFiles() {
        System.out.println("The number of files in the library is: " +
                concreteDao.countFiles());
    }

    public void show(String query){
        if (concreteDao.contains(query)) {
            LogFile file = concreteDao.find(query);
            System.out.println(file);
        }
    }

    public void showAll() {
        ArrayList<String> options = concreteDao.getContent();
        options.forEach(System.out::println);
    }

    public boolean contains(String path) {
        return concreteDao.contains(path);
    }

    public boolean connectionIsEstablished() {
        return concreteDao.connectionIsEstablished();
    }
}
