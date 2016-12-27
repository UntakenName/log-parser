package ru.nc.gordeev.logparser.data.dao;


import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.entity.LogFile;
import ru.nc.gordeev.logparser.data.entity.LogLine;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class FileDbDaoImpl implements IDao {

    private DataSource connectionSource;

    private String getFileID = "SELECT file_id FROM log_file WHERE path=?";

    private String insertFile = "INSERT INTO log_file (path) VALUES (?)";

    private String insertLine = "INSERT INTO log_line (file_id,datetime, mark, log_level, class_path, message)" +
            " VALUES (?,?,?,?,?,?)";

    private String find = "SELECT path, datetime, mark, log_level, class_path, message " +
            "FROM log_file JOIN log_line ON log_file.file_id=log_line.file_id WHERE path=?";

    private String deleteFile = "DELETE FROM log_file WHERE path=?";

    private String deleteLine = "DELETE FROM log_line WHERE file_id=?";

    private String countFiles = "SELECT COUNT(*) FROM log_file";

    private String countLines = "SELECT COUNT(*) FROM log_line WHERE file_id=?";

    private String contains = "SELECT COUNT(*) FROM log_file WHERE path=?";

    public FileDbDaoImpl(DataSource source) {
        connectionSource=source;
    }

    private int getFileID(Connection givenCon,String path) throws SQLException{
        PreparedStatement statement = givenCon.prepareStatement(getFileID);
        statement.setString(1,path);
        ResultSet localRS = statement.executeQuery();
        localRS.next();
        return localRS.getInt("file_id");
    }

    private void insertLogLines(Connection givenCon, int fileID, ArrayList<LogLine> logs) throws SQLException {
        PreparedStatement statement=givenCon.prepareStatement(insertLine);
        statement.setInt(1,fileID);
        for (LogLine line:logs) {
            statement.setTimestamp(2, new Timestamp(line.getDate().getMillis()));
            statement.setString(3,line.getMark());
            statement.setString(4,line.getLogLevel());
            statement.setString(5,line.getClassPath());
            statement.setString(6,line.getMessage());
            statement.executeUpdate();
        }
    }

    ArrayList<LogLine> parseResultSet(ResultSet localRS) throws SQLException{
        ArrayList<LogLine> logs = new ArrayList<> (1000);
        while (localRS.next()) {
            DateTime date = new DateTime(localRS.getTimestamp("datetime").getTime());
            String mark = localRS.getString("mark");
            String logLevel = localRS.getString("log_level");
            String classPath = localRS.getString("class_path");
            String message = localRS.getString("message");
            logs.add(new LogLine(date,mark,logLevel,classPath,message));
        }
        logs.trimToSize();
        return logs;
    }

    @Override
    public void insert(LogFile file) {
        try (Connection localCon = connectionSource.getConnection()) {
            PreparedStatement statement=localCon.prepareStatement(insertFile);
            statement.setString(1,file.getPath());
            statement.executeUpdate();
            int fileID = getFileID(localCon,file.getPath());
            insertLogLines(localCon,fileID,file.getLogs());
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't insert!",e);
        }
    }

    @Override
    public LogFile find(String path) {
        try (Connection localCon = connectionSource.getConnection()) {
            PreparedStatement statement=localCon.prepareStatement(find);
            statement.setString(1,path);
            ResultSet localRS = statement.executeQuery();
            ArrayList<LogLine> logs = parseResultSet(localRS);
            return new LogFile(path,logs);
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't find!",e);
            return null;
        }
    }

    @Override
    public void delete(String path) {
        try (Connection localCon = connectionSource.getConnection()) {
            int fileID = getFileID(localCon,path);
            PreparedStatement statement=localCon.prepareStatement(deleteLine);
            statement.setInt(1,fileID);
            statement.executeUpdate();
            statement=localCon.prepareStatement(deleteFile);
            statement.setString(1,path);
            statement.executeUpdate();
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't delete!",e);
        }
    }

    @Override
    public void update(String path, LogFile file) {
        try (Connection localCon = connectionSource.getConnection()) {
            int fileID = getFileID(localCon,path);
            PreparedStatement statement=localCon.prepareStatement(deleteLine);
            statement.setInt(1,fileID);
            statement.executeUpdate();
            insertLogLines(localCon,fileID,file.getLogs());
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't update!",e);
        }
    }

    @Override
    public int countLines(String path) {
        try (Connection localCon = connectionSource.getConnection()) {
            int fileID = getFileID(localCon,path);
            PreparedStatement statement=localCon.prepareStatement(countLines);
            statement.setInt(1,fileID);
            ResultSet localRS = statement.executeQuery();
            localRS.next();
            return localRS.getInt("COUNT(*)");
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't count!",e);
            return 0;
        }
    }

    @Override
    public int countFiles() {
        try (Connection localCon = connectionSource.getConnection()) {
            ResultSet localRS = localCon.createStatement().executeQuery(countFiles);
            localRS.next();
            return localRS.getInt("COUNT(*)");
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't count!",e);
            return 0;
        }
    }

    @Override
    public ArrayList<String> getContent() {
        ArrayList<String> paths = new ArrayList<>(100);
        try (Connection localCon = connectionSource.getConnection()) {
            ResultSet localRS = localCon.createStatement().executeQuery("SELECT path FROM log_file");
            while (localRS.next()) {
                paths.add(localRS.getString("path"));
            }
            paths.trimToSize();
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't get paths!",e);
        }
        return paths;
    }

    @Override
    public boolean contains(String path) {
        try (Connection localCon = connectionSource.getConnection()) {
            PreparedStatement statement=localCon.prepareStatement(contains);
            statement.setString(1,path);
            ResultSet localRS = statement.executeQuery();
            localRS.next();
            int numberOfEnrties = localRS.getInt("COUNT(*)");
            return numberOfEnrties>0;
        } catch (SQLException e) {
            LoggerFactory.getLogger(FileDbDaoImpl.class).warn("Can't investigate!",e);
            return false;
        }
    }

    @Override
    public boolean connectionIsEstablished() {
        if (connectionSource!=null) {
            try (Connection localCon = connectionSource.getConnection()) {
                return true;
            } catch (SQLException e) {
                return false;
            }
        } else return false;
    }
}
