package arsc;

import common.Util;

public class ResTable_entry {
    private short size;
    private short flags;
    private int resStringPoolRefKey;

    public void init(byte[] fileData, int startIndex){
        size = Util.bytes2short(Util.copyOfRange(fileData, startIndex, 2));
        flags = Util.bytes2short(Util.copyOfRange(fileData, startIndex+2, 2));
        resStringPoolRefKey = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
    }
}
