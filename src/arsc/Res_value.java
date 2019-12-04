package arsc;

import common.Util;
import lombok.Data;

@Data
public class Res_value {
    private short size;
    private byte res0;
    private byte dataType;
    private int data;

    private void init(byte[] fileData, int startIndex){
        size = Util.bytes2short(Util.copyOfRange(fileData, startIndex, 2));
        res0 = fileData[startIndex+2];
        dataType = fileData[startIndex+3];
        data = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
    }
}
