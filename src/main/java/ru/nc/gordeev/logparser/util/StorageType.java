package ru.nc.gordeev.logparser.util;


/** Enum serves to weaken the coupling between the application functionality and applicable storage types.
 *  To make the application work with another type of storage, create DAO implementation of this storage and
 *  declare an element within this enum. Value of the element should be expressed exactly as in the Property
 *  file. The element should contain the full name of the new DAO implementation.
 */

public enum StorageType {
    RAM("ru.nc.gordeev.logparser.util.RAMDAOFactory","ru.nc.gordeev.logparser.data.RAMDAOImpl","ru.nc.gordeev.logparser.data.LineRAMDAOImpl"),
    DB("ru.nc.gordeev.logparser.util.DBDAOFactory","ru.nc.gordeev.logparser.data.FileDBDAOImpl","ru.nc.gordeev.logparser.data.LineDBDAOImpl");
    private String DAOFactoryName;
    private String FileDAOImplName;
    private String LineDAOImplName;

    StorageType(String factoryName, String fileDAOName, String lineDAOName) {
        DAOFactoryName=factoryName;
        FileDAOImplName=fileDAOName;
        LineDAOImplName=lineDAOName;
    }

    public String getDAOFactoryName() {
        return DAOFactoryName;
    }

    public String getFileDAOImplName() {
        return FileDAOImplName;
    }

    public String getLineDAOImplName() {
        return LineDAOImplName;
    }
}
