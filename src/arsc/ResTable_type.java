package arsc;

import common.Util;
import lombok.Data;

@Data
public class ResTable_type {
    private ResChunk_header resChunk_header;
    private byte id;
    private byte flags;
    private short reserved;  // Must be 0.
    private int entryCount;  // // Number of uint32_t entry indices that follow.
    private int entriesStart;
    private ResTable_config resTable_config;

    public void init(byte[] fileData, int startIndex){
        resChunk_header = new ResChunk_header();
        resChunk_header.init(fileData, startIndex);

        id = fileData[startIndex+8];
        flags = fileData[startIndex+9];
        reserved = Util.bytes2short(Util.copyOfRange(fileData, startIndex+0xA, 2));


        entryCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0xC, 4));
        entriesStart = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x10, 4));

        resTable_config = new ResTable_config();
        resTable_config.init(fileData, startIndex+0x14);
    }

}
