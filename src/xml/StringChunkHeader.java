package xml;

import common.Util;
import lombok.Data;

import java.io.UnsupportedEncodingException;

@Data
public class StringChunkHeader {
    private int startOffset;  // 开始偏移处

    private int chunkType;
    private int chunkSize;
    private int stringCount;
    private int styleCount;
    private int unKnow;
    private int stringStart;
    private int styleStart;
    private int[] stringPoolOffset;
    private int[] stylePoolOffset;
//    private int stringOffset;
//    private int styleOffset;
//    private int stringPool;
//    private int stylePool;

    public void init(byte[] fileData, int startIndex){
        startOffset = startIndex;

        chunkType = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0*4, 4));
        chunkSize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+1*4, 4));
        stringCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2*4, 4));
        styleCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+3*4, 4));
        unKnow = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4*4, 4));
        stringStart = Util.bytes2int(Util.copyOfRange(fileData, startIndex+5*4, 4));
        styleStart = Util.bytes2int(Util.copyOfRange(fileData, startIndex+6*4, 4));

        stringPoolOffset = new int[stringCount];
        for(int i=0; i< stringCount; i++){
            stringPoolOffset[i] = Util.bytes2int(Util.copyOfRange(fileData, startIndex+7*4 + i*4, 4));
        }

        stylePoolOffset = new int[styleCount];
        for(int i=0; i< styleCount; i++){
            stylePoolOffset[i] = Util.bytes2int(Util.copyOfRange(fileData, startIndex+7*4 + stringCount*4 +i*4, 4));
        }

    }


    public String getStringByIndex(byte[] fileData, int index) throws UnsupportedEncodingException {
        int stringFileOffset = startOffset + stringStart + stringPoolOffset[index];
        return Util.readU16lenString(fileData, stringFileOffset);
    }

    public String getStyleByIndex(byte[] fileData, int index) throws UnsupportedEncodingException {
        int stringFileOffset = startOffset + styleCount + stylePoolOffset[index];
        return Util.readU16lenString(fileData, stringFileOffset);
    }

}
