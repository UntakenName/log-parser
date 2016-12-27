package ru.nc.gordeev.logparser.data.dao;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.nc.gordeev.logparser.data.entity.LogFile;
import ru.nc.gordeev.logparser.data.entity.LogLine;

import java.util.ArrayList;

import static ru.nc.gordeev.logparser.config.ConfigurationManager.getCurrentConfigurations;

public abstract class AbstractLineDao implements IDao {

    protected Interval toInterval(String query) {
        String pattern = getCurrentConfigurations().getProperties().getProperty("logTimeFormat");
        String subPattern = pattern.substring(0, query.length());
        DateTimeFormatter format = DateTimeFormat.forPattern(subPattern);
        DateTime timeFrom = format.parseDateTime(query);
        for (TimeRange timeRange : TimeRange.values()) {
            if (timeRange.isContainedIn(subPattern)) {
                return timeRange.getInterval(timeFrom, subPattern, pattern);
            }
        }
        long inCaseOfEmergency = DateTime.now().getMillis();
        return new Interval(inCaseOfEmergency, inCaseOfEmergency + 1);
    }

    private enum TimeRange {
        MILLIS(DateTimeFieldType.millisOfSecond(), 999, 0, 0, 'S'),
        SECONDS(DateTimeFieldType.secondOfMinute(), 59, 0, 1, 's'),
        MINUTES(DateTimeFieldType.minuteOfHour(), 59, 0, 1, 'm'),
        HOURS(DateTimeFieldType.hourOfDay(), 23, 0, 1, 'K', 'k', 'H', 'h'),
        DAYS(DateTimeFieldType.dayOfMonth(), 31, 1, 1, 'E', 'e', 'D', 'd') {
            @Override
            protected DateTime getTimeTo(DateTime timeFrom, int fractionTimes, int maxValue) {
                return super.getTimeTo(timeFrom, fractionTimes, timeFrom.dayOfMonth().getMaximumValue());
            }
        },
        MONTHS(DateTimeFieldType.monthOfYear(), 12, 1, 1, 'M'),
        YEARS(DateTimeFieldType.year(), Integer.MAX_VALUE, Integer.MIN_VALUE, 1, 'y', 'Y', 'x') {
            @Override
            protected int specifyFractionTimes(int fractionTimes, int scale) {
                if (scale > 10) {
                    fractionTimes %= scale;
                }
                fractionTimes *= (int) Math.pow(scale, initialScaleMultiplier);
                return fractionTimes;
            }
        };

        protected DateTimeFieldType type;
        protected int maxValue;
        protected int minValue;
        protected int initialScaleMultiplier;
        protected char[] associatedChars;

        public boolean isContainedIn(String pattern) {
            for (char assocChar : associatedChars) {
                if (pattern.indexOf(assocChar) >= 0) {
                    return true;
                }
            }
            return false;
        }

        public Interval getInterval(DateTime timeFrom, String currentFormat, String refFormat) {
            int fractionTimes = timeFrom.get(type);
            int scale = getScale(currentFormat, refFormat);
            fractionTimes = specifyFractionTimes(fractionTimes, scale);
            timeFrom = timeFrom.withField(type, fractionTimes);
            fractionTimes += scale;
            DateTime timeTo = getTimeTo(timeFrom, fractionTimes, maxValue);
            return new Interval(timeFrom.getMillis(), timeTo.getMillis());
        }

        protected int specifyFractionTimes(int fractionTimes, int scale) {
            fractionTimes *= (int) Math.pow(scale, initialScaleMultiplier);
            return fractionTimes;
        }

        protected DateTime getTimeTo(DateTime timeFrom, int fractionTimes, int maxValue) {
            if (fractionTimes > maxValue) {
                TimeRange nextRange = TimeRange.values()[this.ordinal() + 1];
                DateTimeFieldType nextRangeType = nextRange.type;
                fractionTimes = timeFrom.get(nextRangeType) + 1;
                return nextRange.getTimeTo(timeFrom.withField(type, minValue), fractionTimes, nextRange.maxValue);
            } else return timeFrom.withField(type, fractionTimes);
        }

        private int getScale(String currentFormat, String refFormat) {
            for (char associatedChar : associatedChars) {
                if (currentFormat.indexOf(associatedChar) >= 0) {
                    int currentScale = currentFormat.lastIndexOf(associatedChar) -
                            currentFormat.indexOf(associatedChar);
                    int refScale = refFormat.lastIndexOf(associatedChar) -
                            refFormat.indexOf(associatedChar);
                    return (int) Math.pow(10, refScale - currentScale);
                }
            }
            return 1;
        }

        TimeRange(DateTimeFieldType type,
                  int maxValue,
                  int minValue,
                  int initialScaleMultiplier,
                  char... chars) {
            this.type = type;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.initialScaleMultiplier = initialScaleMultiplier;
            associatedChars = chars;
        }
    }

    protected abstract ArrayList<LogLine> findLines(Interval interval);

    @Override
    public void insert(LogFile file) {
    }


    @Override
    public LogFile find(String query) {
        Interval interval = toInterval(query);
        return new LogFile(interval.toString(), findLines(interval));
    }

    @Override
    public void delete(String path) {
    }

    @Override
    public void update(String path, LogFile file) {
    }

    @Override
    public int countLines(String path) {
        return 0;
    }

    @Override
    public int countFiles() {
        return 0;
    }

    @Override
    public ArrayList<String> getContent() {
        return null;
    }

    @Override
    public boolean contains(String path) {
        return false;
    }

    @Override
    public boolean connectionIsEstablished() {
        return false;
    }
}
