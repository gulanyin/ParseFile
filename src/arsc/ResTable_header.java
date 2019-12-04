package arsc;

import common.Util;
import lombok.Data;

@Data
public class ResTable_header {
    private ResChunk_header resChunk_header;
    private int packageCount;

    public void init(byte[] fileData, int startIndex){
        resChunk_header = new ResChunk_header();
        resChunk_header.init(fileData, startIndex);
        packageCount = Util.bytes2short(Util.copyOfRange(fileData, startIndex+0x8, 4));
    }

    public void show(){
        System.out.print("ResTable_header: ");
        System.out.printf("\n  resChunk_header.type: 0x%X", resChunk_header.getType());
        System.out.printf("\n  resChunk_header.headerSize: 0x%X", resChunk_header.getHeaderSize());
        System.out.printf("\n  resChunk_header.size: 0x%X", resChunk_header.getSize());
        System.out.printf("\n  packageCount: 0x%X", packageCount);
    }
}
