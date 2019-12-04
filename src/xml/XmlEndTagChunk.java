package xml;

import common.Util;

import java.io.UnsupportedEncodingException;

public class XmlEndTagChunk extends XmlContent {

    private int unKnow;
    private int namespaceUri;
    private int name;

    public void init(byte[] fileData, int startIndex) {
        chunkType = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        chunkSize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        lineNumber = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2*4, 4));

        unKnow = Util.bytes2int(Util.copyOfRange(fileData, startIndex+3*4, 4));
        namespaceUri = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4*4, 4));
        name = Util.bytes2int(Util.copyOfRange(fileData, startIndex+5*4, 4));
    }


    public void show( byte[] fileData, StringChunkHeader stringChunkHeader) throws UnsupportedEncodingException {
        System.out.printf("\n%s: ", getChunkTypeName());
        System.out.printf("\n  chunkType: 0x%08X", chunkType);
        System.out.printf("\n  chunkSize: 0x%08X", chunkSize);
        System.out.printf("\n  lineNumber: %d", lineNumber);
        System.out.printf("\n  unKnow: 0x%08X", unKnow);
        System.out.printf("\n  namespaceUri: 0x%08X %s",
                namespaceUri,
                namespaceUri >=0 ? stringChunkHeader.getStringByIndex(fileData, namespaceUri) : "");
        System.out.printf("\n  name: 0x%08X %s",
                name,
                name >=0 ? stringChunkHeader.getStringByIndex(fileData, name) : "" );
    }
}
