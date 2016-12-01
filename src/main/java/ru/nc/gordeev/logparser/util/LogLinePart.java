package ru.nc.gordeev.logparser.util;

public enum LogLinePart {
    DATE("date",'d'),
    MARK("mark",'m'),
    LOG_LEVEL("logLevel",'l'),
    CLASS_PATH("classPath",'o'),
    MESSAGE("message",'t');
    public String fieldName;
    public int regExpGroup;
    char placeHolder;
    public int range=0;
    public void setRegExpGroup(int group) {
        regExpGroup=group;
    }
    public void specifyRange(int start, int end) {
        range=end-start+1;
    }
    LogLinePart(String name, char letter) {
        fieldName=name;
        placeHolder=letter;
    }
}
