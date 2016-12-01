package ru.nc.gordeev.logparser.util;

import ru.nc.gordeev.logparser.data.LogFile;

public class DataManager {
    private static DAOFactory factory;
    public static void setFactory(DAOFactory givenFactory) {
        factory=givenFactory;
    }
    public static void insert(LogFile file) {
        factory.getDAOImplementation().insert(file);
    }
    public static LogFile find(String path){
        DAO concreteDAO=factory.getDAOImplementation();
        if (concreteDAO.contains(path)) {
            return concreteDAO.find(path);
        } else return null;
    }
    public static void delete(String path) {
        DAO concreteDAO = factory.getDAOImplementation();
        if (concreteDAO.contains(path)) {
            concreteDAO.delete(path);
        }
    }
    public static void update(String path, LogFile file) {
        DAO concreteDAO=factory.getDAOImplementation();
        if (concreteDAO.contains(path)) {
            concreteDAO.update(path,file);
        }
    }
    public static void countLines(String path) {
        DAO concreteDAO=factory.getDAOImplementation();
        if (concreteDAO.contains(path)) {
            System.out.println("The number of lines in the " + path + " is: " + concreteDAO.countLines(path));
        }
    }
    public static void countFiles() {
        System.out.println("The number of files in the library is: " + factory.getDAOImplementation().countFiles());
    }
    public static void clear() {
        factory.getDAOImplementation().clear();
    }
    public static void show(String path){
        DAO concreteDAO=factory.getDAOImplementation();
        if (concreteDAO.contains(path)) {
            concreteDAO.show(path);
        }
    }
    public static void showAll() {
        factory.getDAOImplementation().showAll();
    }
    public static boolean contains(String path) {
        DAO concreteDAO=factory.getDAOImplementation();
        return concreteDAO.contains(path);
    }
}
