package ru.nc.gordeev.logparser.util;

import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import ru.nc.gordeev.logparser.config.ConfigurationManager;
import ru.nc.gordeev.logparser.config.Configurations;
import ru.nc.gordeev.logparser.config.IConfigurator;
import ru.nc.gordeev.logparser.config.ParsingConfigurator;
import ru.nc.gordeev.logparser.data.DataManager;
import ru.nc.gordeev.logparser.data.entity.LogFile;
import ru.nc.gordeev.logparser.data.entity.LogLine;

import java.util.Properties;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.nc.gordeev.logparser.config.ConfigurationManager.getCurrentConfigurations;


public class ParsingLogicTest {
    LogParser parseTestSubject;
    IConfigurator confTestSubject = new ParsingConfigurator();
    Properties confTestProperties = new Properties();
    Properties initTestProperties = new Properties();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    DataManager parseTestMock = mock(DataManager.class);

    @Mock
    Configurations confTestMock = mock(Configurations.class);

    @Captor
    ArgumentCaptor<LogFile> capturedFile;

    @Captor
    ArgumentCaptor<String> capturedFormat;

    @Before public void initializeProperties() {
        new ConfigurationManager(new ParsingConfigurator()).setInitialConfigurations();
    }


    @Test
    public void parsingAndDistinction() {
        parseTestSubject=new LogParser(parseTestMock,getCurrentConfigurations());

        parseTestSubject.parseFile("test.log");

        verify(parseTestMock).insert(capturedFile.capture());
        LogFile parsedFile = capturedFile.getValue();
        LogLine line = parsedFile.getLogs().get(1);
        assertEquals("Failed to parse!","2016-10-27 18:50:13,655 [   1093]   INFO - etbrains.jps.cmdline.BuildMain - Connection to IDE established in 1027 ms",
                line.toString());
    }

    @Test
    public void patternConfiguration(){
        when(confTestMock.getProperties()).thenReturn(initTestProperties);

        //Desirable and initial formats respectively
        confTestProperties.setProperty("logFormat","t* - oooooooooo [llll] (mmmmmmm) dddd");
        confTestProperties.setProperty("logTimeFormat","yyyy");
        initTestProperties.setProperty("logFormat", "");

        //Configurator should parse the desirable format into these two references
        String regExpRef = "(.*)? - (.{10})? \\[(.{4})?\\] \\((.{7})?\\) (\\d\\d\\d\\d)?";
        String lineToStringFormatRef="%5$s - %4$10s [%3$4s] (%2$7s) %1$4s";

        confTestSubject.setConfiguration(confTestMock,confTestProperties);

        verify(confTestMock).setLogFormat(capturedFormat.capture());
        verify(confTestMock).setLineToStringFormat(capturedFormat.capture());
        String regExp =capturedFormat.getAllValues().get(0);
        String lineToStringFormat = capturedFormat.getAllValues().get(1);

        assertEquals("Failed to build a parsing format!",regExpRef,regExp);
        assertEquals("Failed to build a toString() format",lineToStringFormatRef,lineToStringFormat);
    }
}
