import org.junit.*;
import ru.nc.gordeev.logparser.data.Configurations;
import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.LogLine;
import ru.nc.gordeev.logparser.data.LogLinePart;
import ru.nc.gordeev.logparser.util.*;

import java.util.Properties;

import static junit.framework.TestCase.assertTrue;

public class ParsingLogicTest {

    @Test
    public void parsingAndDistinction() {
        LogParser parser=new LogParser();
        String path = "test.log";
        LogFile parsedFile = parser.parseLog(path,parser.obtainContent("test.log"));
        LogLine line = parsedFile.getLogs().get(1);
        assertTrue("Failed to parse!","1093".equals(line.getMark())
                &&"INFO".equals(line.getLogLevel())
                &&"etbrains.jps.cmdline.BuildMain".equals(line.getClassPath())
                &&"Connection to IDE established in 1027 ms".equals(line.getMessage()));
    }

    @Test
    public void patternConfiguration(){
        Configurator conf = new ParsingConfigurator();
        Properties properties = new Properties();
        properties.setProperty("logFormat","t* - oooooooooo [llll] (mmmmmmm) dddd");
        properties.setProperty("logTimeFormat","yyyy");
        String regExpRef = "(.*)? - (.{10})? \\[(.{4})?\\] \\((.{7})?\\) (\\d\\d\\d\\d)?";
        String lineToStringFormatRef="%5$s - %4$10s [%3$4s] (%2$7s) %1$4s";
        conf.setConfiguration(properties);
        boolean regExpGroupMatch = true;
        for (LogLinePart part:LogLinePart.values()) {
            if (regExpGroupMatch) {
                regExpGroupMatch=(part.regExpGroup==(5-part.ordinal()));
            } else break;
        }
        String regExp = Configurations.getCurrentConfigurations().getLogFormat().toString();
        String lineToStringFormat = Configurations.getCurrentConfigurations().getLineToStringFormat();
        conf.setInitialConfiguration();
        assertTrue("Failed to build a parsing format!",regExpGroupMatch &&
                regExpRef.equals(regExp) && lineToStringFormatRef.equals(lineToStringFormat));
    }
}
