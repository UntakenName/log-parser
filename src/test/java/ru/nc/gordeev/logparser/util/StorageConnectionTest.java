package ru.nc.gordeev.logparser.util;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.nc.gordeev.logparser.data.IDAO;


import java.util.ArrayList;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class StorageConnectionTest {
    IConfigurator storageConfTestSubject = new StorageConfigurator();
    Properties testProperties = new Properties();
    Properties initTestProperties = new Properties();
    ArrayList<String> failedToConnectDAO = new ArrayList<>(2*StorageType.values().length);
    String[] DAOImplType = {"File","Line"};
    IDAO capturedImpl;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Configurations storeTestConfigMock = mock(Configurations.class);

    @Captor
    ArgumentCaptor<IDAO> capturedDAO;

    @Captor
    ArgumentCaptor<IDAOFactory> capturedDAOFactory;

    @Test
    public void storageConnection() {
        when(storeTestConfigMock.getProperties()).thenReturn(initTestProperties);

        initTestProperties.setProperty("whereToStore","");
        testProperties.setProperty("user","system");
        testProperties.setProperty("password","1234");

        //The test tries to establish a connection through each DAOImpl possible
        int numberOfStorages = StorageType.values().length;
        for (StorageType storage: StorageType.values() ) {
            testProperties.setProperty("whereToStore",storage.toString());

            //The test tries to capture a DAOFactory instantiated within the configurator
            storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
            verify(storeTestConfigMock, atMost(numberOfStorages)).setDAOFactory(capturedDAOFactory.capture());

            //The test uses the captured DAOFactory to instantiate a DAO implementation
            doReturn(capturedDAOFactory.getValue()).when(storeTestConfigMock).getDAOFactory();

            //Storage type property removal achieves avoiding a new DAOFactory instantiation
            testProperties.remove("whereToStore");

            //The test try to build a file and a line implementation of the current DAO
            for (String type:DAOImplType) {

                testProperties.setProperty("workWith",type);
                storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
                verify(storeTestConfigMock,atMost(2*numberOfStorages)).setDAO(capturedDAO.capture());

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
