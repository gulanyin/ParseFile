package arsc;

import common.Util;

public class ResTable_entry_map extends ResTable_entry {
    private int resTableRefParent;
    private int count;

    public void init(byte[] fileData, int startIndex){
        super.init(fileData, startIndex);
        resTableRefParent = Util.bytes2int(Util.copyOfRange(fileData, startIndex+8, 4));
        count = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0xC, 4));
    }
}
