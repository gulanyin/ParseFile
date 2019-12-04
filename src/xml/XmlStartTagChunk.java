package xml;

import common.Util;

import java.io.UnsupportedEncodingException;

public class XmlStartTagChunk extends XmlContent {

    private int unKnow;
    private int namespaceUri;
    private int name;
    private int flags;
    private int attributeCount;
    private int classAttribute;
    private int[][] attributes;

    public void init(byte[] fileData, int startIndex) {
        chunkType = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        chunkSize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        lineNumber = Util.bytes2int(Util.copyOfRange(fileData, startIndex+2*4, 4));

        unKnow = Util.bytes2int(Util.copyOfRange(fileData, startIndex+3*4, 4));
        namespaceUri = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4*4, 4));
        name = Util.bytes2int(Util.copyOfRange(fileData, startIndex+5*4, 4));
        flags = Util.bytes2int(Util.copyOfRange(fileData, startIndex+6*4, 4));
        attributeCount = Util.bytes2int(Util.copyOfRange(fileData, startIndex+7*4, 4));
        classAttribute = Util.bytes2int(Util.copyOfRange(fileData, startIndex+8*4, 4));

        attributes = new int[attributeCount][5];

        for(int i=0; i< attributeCount; i++){
            for(int j=0; j<5; j++){
                attributes[i][j] = Util.bytes2int(Util.copyOfRange(fileData, startIndex+9*4 + i*20 + j*4, 4));
            }
        }

    }

    public void show( byte[] fileData, StringChunkHeader stringChunkHeader) throws UnsupportedEncodingException {
        System.out.printf("\n%s: ", getChunkTypeName());
        System.out.printf("\n  chunkType: 0x%X", chunkType);
        System.out.printf("\n  chunkSize: 0x%X", chunkSize);
        System.out.printf("\n  lineNumber: %d", lineNumber);
        System.out.printf("\n  unKnow: 0x%X", unKnow);
        System.out.printf("\n  namespaceUri: 0x%X %s",
                namespaceUri,
                namespaceUri >=0 ? stringChunkHeader.getStringByIndex(fileData, namespaceUri): "");
        System.out.printf("\n  uri: 0x%X %s",
                name,
                name >=0 ? stringChunkHeader.getStringByIndex(fileData, name) : "");
        System.out.printf("\n  flags: 0x%X", flags);
        System.out.printf("\n  attributeCount: 0x%X", attributeCount);
        System.out.printf("\n  classAttribute: 0x%X", classAttribute);
        for(int i=0; i<attributeCount; i++){
            System.out.printf("\n      attribute # %d:", i);
            System.out.printf("\n        NamespaceUri: 0x%08X  %s",
                    attributes[i][0],
                    attributes[i][0] >=0 ? stringChunkHeader.getStringByIndex(fileData, attributes[i][0]): "");
            System.out.printf("\n        name: 0x%08X  %s",
                    attributes[i][1],
                    attributes[i][1] >=0 ? stringChunkHeader.getStringByIndex(fileData, attributes[i][1]): "");
            System.out.printf("\n        valueStr: 0x%08X", attributes[i][2]);
            System.out.printf("\n        type: 0x%08X", attributes[i][3]);
            System.out.printf("\n        data: 0x%08X", attributes[i][4]);
        }

    }
}
