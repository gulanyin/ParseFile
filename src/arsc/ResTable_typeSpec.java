package arsc;

import common.Util;
import lombok.Data;

@Data
public class ResTable_typeSpec {
    private ResChunk_header resChunk_header;
    private byte id;
    private byte res0;
    private short res1;
    private int entryCount;

    public void init(byte[] fileData, int startIndex){
        resChunk_header = new ResChunk_header();
        resChunk_header.init(fileData, startIndex);
        startIndex += 8;
        id = fileData[startIndex];
        res0 = fileData[startIndex+1];
        res1 = Util.bytes2short(Util.copyOfRange(fileData, startIndex+2, 2));
        entryCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2, 4));
    }

}
