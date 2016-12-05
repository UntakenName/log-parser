package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

import static ru.nc.gordeev.logparser.util.ConfigurationManager.*;

public class DataManager{

    private IDAO concreteDAO;

    DataManager() {
        concreteDAO=getCurrentConfigurations().getDAO();
    }

    public void insert(LogFile file) {
        if (!concreteDAO.contains(file.getPath())) {
            concreteDAO.insert(file);
            System.out.println(file.getPath() + " has been put in the library.");
        } else concreteDAO.update(file.getPath(),file);
    }
    public LogFile find(String path){
        if (concreteDAO.contains(path)) {
            return concreteDAO.find(path);
        } else return null;
    }

    public void delete(String path) {
        if (concreteDAO.contains(path)) {
            concreteDAO.delete(path);
        }
    }

    public void update(String path, LogFile file) {
        if (concreteDAO.contains(path)) {
            concreteDAO.update(path,file);
            System.out.println(path + " has been updated.");
        } else System.out.println("No such file on the library");
    }

    public void countLines(String path) {
        if (concreteDAO.contains(path)) {
            System.out.println("The number of lines in the " + path + " is: "
                    + concreteDAO.countLines(path));
        }
    }

    public void countFiles() {
        System.out.println("The number of files in the library is: " +
                concreteDAO.countFiles());
    }

    public void clear(){concreteDAO.clear();
    }

    public void show(String path){
        if (concreteDAO.contains(path)) {
            concreteDAO.show(path);
        }
    }

    public void showAll() {
        concreteDAO.showAll();
    }

    public boolean contains(String path) {
        return concreteDAO.contains(path);
    }

    public boolean connectionIsEstablished() {
        return concreteDAO.connectionIsEstablished();
    }
}
