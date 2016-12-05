package ru.nc.gordeev.logparser.util;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.nc.gordeev.logparser.data.Configurations;

import ru.nc.gordeev.logparser.data.StorageType;


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

        //The test tries to establish a connection through each DAOImpl possible
        for (StorageType storage: StorageType.values() ) {
            testProperties.setProperty("whereToStore",storage.toString());

            //The test tries to capture a instantiated within the configurator DAOFactory
            storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
            verify(storeTestConfigMock).setDAOFactory(capturedDAOFactory.capture());

            //The test uses the captured DAOFactory to instantiate a DAO implementation
            when(storeTestConfigMock.getDAOFactory()).thenReturn(capturedDAOFactory.getValue());

            //Storage type property removal achieves avoiding a new DAOFactory instantiation
            testProperties.remove("whereToStore");

            //The test try to build a file and a line implementation of the current DAO
            for (String type:DAOImplType) {

                testProperties.setProperty("workWith",type);
                storageConfTestSubject.setConfiguration(storeTestConfigMock,testProperties);
                verify(storeTestConfigMock).setDAO(capturedDAO.capture());

                //If getValue() returns the same object as capturedImpl, The factory failed to instantiate a DAOImpl
                if (capturedDAO.getValue()==capturedImpl||!capturedDAO.getValue().connectionIsEstablished()) {
                    failedToConnectDAO.add(type+storage+"DAOImpl");
                }
                capturedImpl = capturedDAO.getValue();
            }
            testProperties.remove("workWith");
        }
        assertTrue("Failed to connect with:\n"+failedToConnectDAO+"\n",failedToConnectDAO.size()==0);
    }
}
