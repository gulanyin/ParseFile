package arsc;

import common.Util;
import lombok.Data;

import java.io.UnsupportedEncodingException;

@Data
public class ResStringPool_header {
    public final static int SORTED_FLAG = 1<<0;
    public final static int UTF8_FLAG  = 1<<8;

    private ResChunk_header resChunk_header;
    private int stringCount;
    private int styleCount;
    private int flags;
    private int stringsStart;
    private int stylesStart;


    public void init(byte[] fileData, int startIndex){
        resChunk_header = new ResChunk_header();
        resChunk_header.init(fileData, startIndex);

        startIndex += 8;
        stringCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        styleCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        flags = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2*4, 4));
        stringsStart = Util.bytes2int(Util.copyOfRange(fileData, startIndex+3*4, 4));
        stylesStart = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4*4, 4));
    }

    public String getStringByIndex(byte[] originFileData, ResTable_header resTable_header, int index) throws UnsupportedEncodingException {
        int indexOffset = resTable_header.getResChunk_header().getHeaderSize() + resChunk_header.getHeaderSize() + index*4;
        int stringOffset = Util.readInt(originFileData, indexOffset) + stringsStart + resTable_header.getResChunk_header().getHeaderSize();
        int u16len = originFileData[stringOffset] & 0xFF;
        int u8len = originFileData[stringOffset+1] & 0xFF;
        return Util.readUtf8String(originFileData, stringOffset+2, u8len);
    }

    public String getStyleByIndex(byte[] originFileData, ResTable_header resTable_header, int index) throws UnsupportedEncodingException {
        int indexOffset = resTable_header.getResChunk_header().getHeaderSize() + resChunk_header.getHeaderSize()+ stringCount*4 + index*4;
        int stringOffset = resTable_header.getResChunk_header().getHeaderSize() + stylesStart + Util.readInt(originFileData, indexOffset);
        int u16len = originFileData[stringOffset] & 0xFF;
        int u8len = originFileData[stringOffset+1] & 0xFF;
        return Util.readUtf8String(originFileData, stringOffset+2, u8len);
    }

    public void show(byte[] originFileData, ResTable_header resTable_header) throws UnsupportedEncodingException {
        System.out.printf("\nResStringPool : %d %08X  %d %08X", SORTED_FLAG, SORTED_FLAG, UTF8_FLAG, UTF8_FLAG);
        System.out.printf("\n  stringCount : %d", stringCount);
        System.out.printf("\n  styleCount : %d", styleCount);
        System.out.printf("\n  flags : 0x%X", flags);
        System.out.printf("\n  stringsStart : %X", stringsStart);
        System.out.printf("\n  stylesStart : %X", stylesStart);
        System.out.printf("\n  strings : %X", stylesStart);


        int numWidth = (stringCount+"").length();
        String numColWidth = String.format("[%%%ds]", numWidth);
        for(int i=0; i<stringCount; i++){
            System.out.printf("\n    "+numColWidth+" %s", i, getStringByIndex(originFileData, resTable_header, i));
        }

        numWidth = (styleCount+"").length();
        numColWidth = String.format("[%%%ds]", numWidth);
        for(int i=0; i<styleCount; i++){
            System.out.printf("\n    "+numColWidth+" %s", i, getStyleByIndex(originFileData, resTable_header, i));
        }


    }
}
