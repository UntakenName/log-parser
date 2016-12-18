package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.IDAO;
import ru.nc.gordeev.logparser.data.LogFile;

import java.util.ArrayList;

import static ru.nc.gordeev.logparser.util.ConfigurationManager.*;

public class DataManager{

    private IDAO concreteDAO;

    public DataManager() {
        concreteDAO=getCurrentConfigurations().getDAO();
    }

    public void insert(LogFile file) {
        if (!concreteDAO.contains(file.getPath())) {
            concreteDAO.insert(file);
            System.out.println(file.getPath() + " has been put in the library.");
        } else System.out.println("File with the same path has been already parsed to the library.\nChange the path or Update the file. ");
    }
    public LogFile find(String query){
        if (concreteDAO.contains(query)) {
            return concreteDAO.find(query);
        } else return null;
    }

    public void delete(String query) {
        if (concreteDAO.contains(query)) {
            concreteDAO.delete(query);
        }
    }

    public void update(String path, LogFile file) {
        if (concreteDAO.contains(path)) {
            concreteDAO.update(path,file);
            System.out.println(path + " has been updated.");
        } else System.out.println("No such file on the library");
    }

    public void countLines(String query) {
        if (concreteDAO.contains(query)) {
            System.out.println("The number of lines in the " + query + " is: "
                    + concreteDAO.countLines(query));
        }
    }

    public void countFiles() {
        System.out.println("The number of files in the library is: " +
                concreteDAO.countFiles());
    }

    public void show(String query){
        if (concreteDAO.contains(query)) {
            LogFile file = concreteDAO.find(query);
            System.out.println(file);
        }
    }

    public void showAll() {
        ArrayList<String> options = concreteDAO.getContent();
        options.forEach(System.out::println);
    }

    public boolean contains(String path) {
        return concreteDAO.contains(path);
    }

    public boolean connectionIsEstablished() {
        return concreteDAO.connectionIsEstablished();
    }
}
