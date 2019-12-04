package xml;

import common.Util;
import lombok.Data;

import java.io.UnsupportedEncodingException;

@Data
public class XmlNamespaceChunk extends XmlContent {

    private int unKnow;
    private int prefix;
    private int uri;

    public void init(byte[] fileData, int startIndex) {
        chunkType = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        chunkSize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        lineNumber = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2*4, 4));
        unKnow = Util.bytes2int(Util.copyOfRange(fileData, startIndex+3*4, 4));
        prefix = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4*4, 4));
        uri = Util.bytes2int(Util.copyOfRange(fileData, startIndex+5*4, 4));
    }

    public void show( byte[] fileData, StringChunkHeader stringChunkHeader) throws UnsupportedEncodingException {
        System.out.printf("\n%s: ", getChunkTypeName());
        System.out.printf("\n  chunkType: 0x%X", chunkType);
        System.out.printf("\n  chunkSize: 0x%X", chunkSize);
        System.out.printf("\n  lineNumber: %d", lineNumber);
        System.out.printf("\n  unKnow: 0x%X", unKnow);
        System.out.printf("\n  prefix: 0x%X %s",
                prefix,
                stringChunkHeader.getStringByIndex(fileData, prefix));
        System.out.printf("\n  uri: 0x%X %s",
                uri,
                stringChunkHeader.getStringByIndex(fileData, uri) );

    }
}
