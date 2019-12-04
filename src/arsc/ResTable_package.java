package arsc;

import common.Util;

import java.io.UnsupportedEncodingException;

public class ResTable_package {
    private ResChunk_header resChunk_header;
    private int id;
    private String name; // = new short[128];
    private int typeStrings;
    private int lastPublicType;
    private int keyStrings;
    private int lastPublicKey;
    private int typeIdOffset;

    public void init(byte[] fileData, int startIndex) throws UnsupportedEncodingException {
        resChunk_header = new ResChunk_header();
        resChunk_header.init(fileData, startIndex);

        startIndex += 8;
        id = Util.readInt(fileData, startIndex);
        name = Util.readU16lenString(fileData, startIndex+4);

        startIndex += (2*128+4);
        typeStrings = Util.readInt(fileData, startIndex);
        lastPublicType = Util.readInt(fileData, startIndex+1*4);
        keyStrings = Util.readInt(fileData, startIndex+2*4);
        lastPublicKey = Util.readInt(fileData, startIndex+3*4);
        typeIdOffset = Util.readInt(fileData, startIndex+4*4);
    }

    public void show(byte[] fileData, int startIndex) throws UnsupportedEncodingException {
        System.out.print("\nResTable_package: ");
        System.out.printf("\n  id: 0x%08X", id);
        System.out.printf("\n  name: %s", name);
        System.out.printf("\n  typeStrings: 0x%X", typeStrings);
        System.out.printf("\n  lastPublicType: 0x%X", lastPublicType);
        System.out.printf("\n  keyStrings: 0x%X", keyStrings);
        System.out.printf("\n  lastPublicKey: 0x%X", lastPublicKey);
        System.out.printf("\n  typeIdOffset: 0x%X", typeIdOffset);
        System.out.printf("\n  startIndex: 0x%X", startIndex);

        // startIndex 是ResTable_package 在文件中的偏移上
        System.out.print("\n  typeStringPool: ");
        int currentStructStartIndex = startIndex + typeStrings;  // 当前结构的偏移
        if(typeStrings >0){
            ResStringPool_header stringPool = new ResStringPool_header();
            // startIndex + typeStrings 是 stringPoolType 开始的地方
            stringPool.init(fileData, currentStructStartIndex);

            // stringIndexArrayStartIndex 是 偏移数组的起始地方
            int stringIndexArrayStartIndex = currentStructStartIndex + stringPool.getResChunk_header().getHeaderSize();
            // 字符串开始的地方
            int stringPoolStartIndex =  currentStructStartIndex +stringPool.getStringsStart();
            int stringCount = stringPool.getStringCount();
            int numWidth = (stringCount+"").length();
            String numColWidth = String.format("[%%%ds]", numWidth);

            for(int j =0; j< stringPool.getStringCount(); j++){
                int realOffset = stringPoolStartIndex + Util.readInt(fileData, stringIndexArrayStartIndex + j*4);
                String realString = "";
                if(stringPool.getFlags() == 0x0){  //  为 0 是两个字节代表一个字符
                    realString = Util.readU16lenString(fileData, realOffset);
                }else if(stringPool.getFlags() == 0x100){ //  为 100 是两个字节代表一个字符
                    int u16len = fileData[realOffset] & 0xFF;
                    int u8len = fileData[realOffset+1] & 0xFF;
                    realString = Util.readUtf8String(fileData, realOffset+2, u8len);
                }

                System.out.printf("\n    "+numColWidth+" %s", j, realString);
            }

            currentStructStartIndex += stringPool.getResChunk_header().getSize();
        }

        System.out.print("\n  keyStringPool: ");
        if(keyStrings > 0){
            ResStringPool_header stringPool = new ResStringPool_header();
            // startIndex + typeStrings 是 stringPoolType 开始的地方
            stringPool.init(fileData, currentStructStartIndex);

            // stringIndexArrayStartIndex 是 偏移数组的起始地方
            int stringIndexArrayStartIndex = currentStructStartIndex + stringPool.getResChunk_header().getHeaderSize();
            // 字符串开始的地方
            int stringPoolStartIndex =  currentStructStartIndex +stringPool.getStringsStart();
            int stringCount = stringPool.getStringCount();
            int numWidth = (stringCount+"").length();
            String numColWidth = String.format("[%%%ds]", numWidth);

            for(int j =0; j< stringPool.getStringCount(); j++){
                int realOffset = stringPoolStartIndex + Util.readInt(fileData, stringIndexArrayStartIndex + j*4);
                String realString = "";
                if(stringPool.getFlags() == 0x0){  //  为 0 是两个字节代表一个字符
                    realString = Util.readU16lenString(fileData, realOffset);
                }else if(stringPool.getFlags() == 0x100){ //  为 100 是两个字节代表一个字符
                    int u16len = fileData[realOffset] & 0xFF;
                    int u8len = fileData[realOffset+1] & 0xFF;
                    realString = Util.readUtf8String(fileData, realOffset+2, u8len);
                }
//                System.out.printf("\n    "+numColWidth+" %s", j, realString);
            }

            currentStructStartIndex += stringPool.getResChunk_header().getSize();
        }


        // 后面是 ResTable_typeSpec 和 ResTable_type 不定顺序交替出现
        while(currentStructStartIndex < fileData.length){
            ResChunk_header chunkHeader = new ResChunk_header();
            chunkHeader.init(fileData, currentStructStartIndex);
            if(ResChunk_header.RES_TABLE_TYPE_SPEC_TYPE == chunkHeader.getType()){
                ResTable_typeSpec resTableTypeSpec = new ResTable_typeSpec();
                resTableTypeSpec.init(fileData, currentStructStartIndex);
                System.out.printf("\n  RES_TABLE_TYPE_SPEC: ");
                System.out.printf("\n      id: 0x%X", resTableTypeSpec.getId());
                System.out.printf("\n      entryCount: %d", resTableTypeSpec.getEntryCount());

            }else if(ResChunk_header.RES_TABLE_TYPE_TYPE == chunkHeader.getType()){
                ResTable_type resTableType = new ResTable_type();
                resTableType.init(fileData, currentStructStartIndex);
                System.out.printf("\n  RES_TABLE_TYPE: ");
            }

            currentStructStartIndex += chunkHeader.getSize();
        }

    }
}
