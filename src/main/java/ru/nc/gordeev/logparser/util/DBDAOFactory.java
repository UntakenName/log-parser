package ru.nc.gordeev.logparser.util;


import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.IDAO;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.Locale;
import java.util.Properties;

public class DBDAOFactory implements IDAOFactory {
    private HikariDataSource connectionPool;

    public DBDAOFactory(Properties credentials) {

        Locale.setDefault(Locale.US);
        connectionPool=new HikariDataSource();
        String url = credentials.getProperty("url");
        if (url==null) {
            url="jdbc:oracle:thin:@localhost:1521:XE";
        }
        String user = credentials.getProperty("user");
        String password = credentials.getProperty("password");

        connectionPool.setJdbcUrl(url);
        connectionPool.setUsername(user);
        connectionPool.setPassword(password);

        try (Connection connection = connectionPool.getConnection()) {
            Statement statement = connection.createStatement();
            String initialization =
                    "DECLARE " +
                    "table_count INTEGER; " +
                    "BEGIN " +
                    "table_count:=0; "+
                    "SELECT COUNT(*) " +
                    "INTO table_count " +
                    "FROM user_tables " +
                    "WHERE table_name = 'LOG_FILE'; " +
                    "IF table_count = 0 THEN " +
                    "EXECUTE IMMEDIATE " +
                    "'CREATE TABLE log_file " +
                    "(file_id INTEGER NOT NULL PRIMARY KEY, " +
                    "path VARCHAR(260))'; " +
                    "EXECUTE IMMEDIATE "+
                    "'CREATE TABLE log_line " +
                    "(line_id INTEGER NOT NULL PRIMARY KEY, " +
                    "file_id INTEGER, " +
                    "datetime TIMESTAMP(3), " +
                    "mark VARCHAR(100), " +
                    "log_level VARCHAR(100), " +
                    "class_path VARCHAR(100)," +
                    "message VARCHAR(4000)," +
                    "FOREIGN KEY (file_id) REFERENCES log_file(file_id))'; " +
                    "EXECUTE IMMEDIATE "+
                    "'CREATE SEQUENCE log_file_id_seq START WITH 1'; " +
                    "EXECUTE IMMEDIATE " +
                    "('CREATE OR REPLACE TRIGGER trg_log_file_id " +
                    "BEFORE INSERT ON log_file " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT log_file_id_seq.nextval INTO :new.file_id FROM dual; " +
                    "END;'); " +
                    "EXECUTE IMMEDIATE " +
                    "'CREATE SEQUENCE log_line_id_seq START WITH 1'; " +
                    "EXECUTE IMMEDIATE " +
                    "('CREATE OR REPLACE TRIGGER trg_log_line_id " +
                    "BEFORE INSERT ON log_line " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT log_line_id_seq.nextval INTO :new.line_id FROM dual; " +
                    "END;'); " +
                    "END IF; " +
                    "END;";
            statement.execute(initialization);
            System.out.println("DataBase type of data storage has been applied!");
        } catch (SQLException e) {
            LoggerFactory.getLogger(DBDAOFactory.class).error("Can't connect to the database!",e);
        }
    }

    @Override
    public IDAO getDAOImplementation(String DAOType) throws
            ClassNotFoundException, InstantiationException,IllegalAccessException,NoSuchMethodException, InvocationTargetException{
        switch (DAOType.toLowerCase()) {
            case "line":
                DAOType= StorageType.DB.getLineDAOImplName();
                break;
            case "file":
                DAOType=StorageType.DB.getFileDAOImplName();
                break;
        }
        Class DAOClass = Class.forName(DAOType);
        Constructor constructor = DAOClass.getConstructor(DataSource.class);
        return (IDAO) constructor.newInstance(connectionPool);
    }

    @Override
    protected void finalize() throws Throwable{
        connectionPool.close();
        super.finalize();
    }
}
