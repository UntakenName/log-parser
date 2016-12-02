import org.junit.*;
import ru.nc.gordeev.logparser.data.LogFile;
import ru.nc.gordeev.logparser.data.LogLine;
import ru.nc.gordeev.logparser.util.*;

import static junit.framework.TestCase.assertTrue;

public class ParsingTest {

    @Test
    public void parseLog() {
        new ConfigurationManager(new StorageConfigurator(),new ParsingConfigurator(),new DateConfigurator())
                .setInitialConfigurations();
        LogParser parser=new LogParser();
        String path = "test.log";
        LogFile parsedFile = parser.parseLog(path,parser.obtainContent("test.log"));
        LogLine line = parsedFile.getLogs().get(1);
        assertTrue("Failed to parse!","1093".equals(line.getMark())
                &&"INFO".equals(line.getLogLevel())
                &&"etbrains.jps.cmdline.BuildMain".equals(line.getClassPath())
                &&"Connection to IDE established in 1027 ms".equals(line.getMessage()));
    }
}
