package ru.nc.gordeev.logparser.data.dao;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.LoggerFactory;
import ru.nc.gordeev.logparser.data.entity.LogFile;
import ru.nc.gordeev.logparser.data.entity.LogLine;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import static ru.nc.gordeev.logparser.config.ConfigurationManager.*;

public class LineDbDaoImpl extends AbstractLineDao {

    private DataSource connectionSource;
    private FileDbDaoImpl aid;

    private String findLines = "SELECT datetime, mark, log_level, class_path, message " +
            "FROM log_line WHERE datetime BETWEEN ? AND ?";

    private String countLines = "SELECT COUNT(*) FROM log_line WHERE datetime BETWEEN ? AND ?";

    private String getContent = "SELECT MAX(datetime), MIN(datetime) FROM Log_line";

    private String contains = "SELECT COUNT(*) FROM log_line WHERE ROWNUM=1 AND datetime BETWEEN ? AND ?";

    private String delete = "DELETE FROM log_line WHERE datetime BETWEEN ? AND ?";

    public LineDbDaoImpl(DataSource source) {
        connectionSource=source;
        aid = new FileDbDaoImpl(source);
    }

    private ResultSet executeQueryForInterval(Connection localCon, String query,Interval interval)
        throws SQLException{
        PreparedStatement statement = localCon.prepareStatement(query);
        statement.setTimestamp(1,new Timestamp(interval.getStart().getMillis()));
        statement.setTimestamp(2,new Timestamp(interval.getEnd().getMillis()));
        return statement.executeQuery();
    }

    @Override
    protected ArrayList<LogLine> findLines(Interval interval) {
        try (Connection localCon = connectionSource.getConnection()){
            ResultSet localRS = executeQueryForInterval(localCon,findLines,interval);
            return aid.parseResultSet(localRS);
        } catch (SQLException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't find!",e);
            return null;
        }
    }

    @Override
    public void insert(LogFile file) {
        if(!aid.contains(file.getPath())) {
            aid.insert(file);
        }
    }

    @Override
    public void delete(String query) {
        Interval interval = toInterval(query);
        try (Connection localCon = connectionSource.getConnection()) {
            executeQueryForInterval(localCon,delete,interval);
        } catch (SQLException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't delete!",e);
        }
    }


    @Override
    public void update(String path, LogFile file) {
        aid.update(path,file);
    }

    @Override
    public int countLines(String query) {
        Interval interval = toInterval(query);
        try (Connection localCon = connectionSource.getConnection()) {
            ResultSet localRS = executeQueryForInterval(localCon,countLines,interval);
            localRS.next();
            return localRS.getInt("COUNT(*)");
        } catch (SQLException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't count!",e);
            return 0;
        }
    }

    @Override
    public int countFiles() {
        return aid.countFiles();
    }

    @Override
    public ArrayList<String> getContent() {
        try (Connection localCon = connectionSource.getConnection()) {
            PreparedStatement statement = localCon.prepareStatement(getContent);
            ResultSet localRS = statement.executeQuery();
            localRS.next();
            DateTime first = new DateTime(localRS.getTimestamp("MIN(datetime)").getTime());
            DateTime last = new DateTime(localRS.getTimestamp("MAX(datetime)").getTime());
            ArrayList<String> timeRange = new ArrayList<>(2);
            timeRange.add(first.toString(getCurrentConfigurations().getLogTimeFormat()));
            timeRange.add(last.toString(getCurrentConfigurations().getLogTimeFormat()));
            return timeRange;
        } catch (SQLException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't get content!",e);
            return null;
        }
    }

    @Override
    public boolean contains(String query) {
        try (Connection localCon = connectionSource.getConnection()) {
            Interval interval = toInterval(query);
            ResultSet localRS = executeQueryForInterval(localCon,contains,interval);
            localRS.next();
            return localRS.getInt("COUNT(*)")>0;
        } catch (SQLException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't investigate!",e);
            return false;
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(LineDbDaoImpl.class).warn("Can't parse DateTime out of the query!",e);
            return false;
        }
    }

    @Override
    public boolean connectionIsEstablished() {
        return aid.connectionIsEstablished();
    }
}
