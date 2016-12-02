package ru.nc.gordeev.logparser.data;

/** Enum serves to weaken the coupling between the application functionality and applicable storage types.
 *  To make the application work with another type of storage, create DAO implementation of this storage and
 *  declare an element within this enum. Value of the element should be expressed exactly as in the Property
 *  file. The element should contain the full name of the new DAO implementation.
 */

public enum StorageType {
    RAM("ru.nc.gordeev.logparser.util.RAMDAOImpl");
    public String DAOImplementationName;
    StorageType(String name) {
        DAOImplementationName=name;
    }
}
