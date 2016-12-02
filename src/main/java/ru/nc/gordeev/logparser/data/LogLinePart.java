package ru.nc.gordeev.logparser.data;

/** Enum serves to weaken the coupling between the utilitarian classes and the data consisted in a logline.
 *  To make another field within LogLine class, declare it and its toString(), its getters and setters and
 *  declare a new element of this enum as well as a placeholder in properties for the new logline part.
 */

public enum LogLinePart {
    DATE('d'),
    MARK('m'),
    LOG_LEVEL('l'),
    CLASS_PATH('o'),
    MESSAGE('t');
    public int regExpGroup;
    public char placeHolder;
    public int range=0;
    public void setRegExpGroup(int group) {
        regExpGroup=group;
    }
    public void specifyRange(int start, int end) {
        range=end-start+1;
    }
    LogLinePart(char letter) {
        placeHolder=letter;
    }
}
