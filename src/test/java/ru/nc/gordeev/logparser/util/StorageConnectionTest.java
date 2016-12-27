package ru.nc.gordeev.logparser.util;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.nc.gordeev.logparser.config.ConfigurationManager;
import ru.nc.gordeev.logparser.config.Configurations;
import ru.nc.gordeev.logparser.config.IConfigurator;
import ru.nc.gordeev.logparser.config.StorageConfigurator;
import ru.nc.gordeev.logparser.data.dao.IDao;
import ru.nc.gordeev.logparser.data.dao.factory.IDaoFactory;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class StorageConnectionTest {
    IConfigurator storageConfTestSubject = new StorageConfigurator();
    HikariDataSource testDbConnectionPool = new HikariDataSource();
    Properties testProperties = new Properties();
    Properties initTestProperties = new Properties();
    ArrayList<String> failedToConnectDAO = new ArrayList<>(2*StorageType.values().length);
    String[] DAOImplType = {"File","Line"};
    IDao capturedImpl;


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Configurations storeTestConfigMock = mock(Configurations.class);

    @Captor
    ArgumentCaptor<IDao> capturedDAO;

    @Captor
    ArgumentCaptor<IDaoFactory> capturedDAOFactory;

    @Before
    public void initializeDbConnectionPool() {
        Locale.setDefault(Locale.US);
        testDbConnectionPool.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:XE");
        testDbConnectionPool.setUsername("system");
        testDbConnectionPool.setPassword("1234");
    }

    @Test
    public void storageConnection() {
        when(storeTestConfigMock.getProperties()).thenReturn(initTestProperties);
        when(storeTestConfigMock.getConnectionSource()).thenReturn(testDbConnectionPool);

        initTestProperties.setProperty("whereToStore","");
        testProperties.setProperty("user","system");
        testProperties.setProperty("password","1234");

        //The test tries to establish a connection through each DAOImpl possible
        int numberOfStorages = StorageType.values().length;
        for (StorageType storage: StorageType.values() ) {
            testProperties.setProperty("whereToStore",storage.toString());

            //The test tries to capture a DAOFactory instantiated within the configurator
            storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
            verify(storeTestConfigMock, atMost(numberOfStorages)).setDaoFactory(capturedDAOFactory.capture());

            //The test uses the captured DAOFactory to instantiate a DAO implementation
            doReturn(capturedDAOFactory.getValue()).when(storeTestConfigMock).getDaoFactory();

            //Storage type property removal achieves avoiding a new DAOFactory instantiation
            testProperties.remove("whereToStore");

            //The test try to build a file and a line implementation of the current DAO
            for (String type:DAOImplType) {

                testProperties.setProperty("workWith",type);
                storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
                verify(storeTestConfigMock,atMost(2*numberOfStorages)).setDao(capturedDAO.capture());

                //If getValue() returns the same object as capturedImpl, The factory failed to instantiate a DAOImpl
                if (capturedDAO.getValue()==capturedImpl||!capturedDAO.getValue().connectionIsEstablished()) {
                    failedToConnectDAO.add(type+storage+"DAOImpl");
                }
                capturedImpl = capturedDAO.getValue();
            }

            //DAOImpl type property removal achieves avoiding a DAOIpml instantiation in the wrapper for-cycle
            testProperties.remove("workWith");
        }
        assertTrue("Failed to connect with:\n"+failedToConnectDAO,failedToConnectDAO.size()==0);
    }
}
