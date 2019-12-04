package dex;

import common.Util;
import lombok.Data;

@Data
public class DexFieldId {
    private short classIdx;
    private short typeIdx;
    private int nameIdx;

    public void init(byte[] fileDate, int startIndex){
        classIdx = Util.bytes2short(Util.copyOfRange(fileDate, startIndex, 2));
        typeIdx = Util.bytes2short(Util.copyOfRange(fileDate, startIndex+2, 2));
        nameIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex+4, 4));
    }
}
