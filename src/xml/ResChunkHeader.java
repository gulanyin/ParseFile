package xml;

import common.Util;
import lombok.Data;

@Data
public class ResChunkHeader {
    private short type;
    private short heardSize;
    private int size;

    public void init(byte[] fileData){
        type = Util.bytes2short(Util.copyOfRange(fileData, 0, 2));
        heardSize = Util.bytes2short(Util.copyOfRange(fileData, 2, 2));
        size = Util.bytes2int(Util.copyOfRange(fileData, 4, 4));
    }

}
