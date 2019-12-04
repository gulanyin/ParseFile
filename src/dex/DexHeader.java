package dex;
import common.Util;
import lombok.Data;

import java.util.Arrays;

@Data
public class DexHeader {
    private byte magic[] = new byte[8];
    private int checkSum;
    private byte signature[] = new byte[20];
    private int fileSize;
    private int headerSize;
    private int endianTag;
    private int linkSize;
    private int linkOff;
    private int mapOff;
    private int stringIdsSize;
    private int stringIdsOff;
    private int typeIdsSIze;
    private int typeIdsOff;
    private int protoIdsSize;
    private int protoIdsOff;
    private int fieldIdsSize;
    private int fieldIdsOff;
    private int methodIdsSize;
    private int methodIdsOff;
    private int classDefsSize;
    private int classDefsOff;
    private int dataSize;
    private int dataOff;


    public void init(byte fileDate[]){
        int startIndex = 0x0;
        this.magic = Arrays.copyOfRange(fileDate, startIndex, startIndex+8);
        startIndex = 0x8;
        checkSum = Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0xC;
        signature = Arrays.copyOfRange(fileDate, startIndex, startIndex+20);
        startIndex = 0x20;
        fileSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x24;
        headerSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x28;
        endianTag =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x2C;
        linkSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x30;
        linkOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x34;
        mapOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x38;
        stringIdsSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x3C;
        stringIdsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x40;
        typeIdsSIze =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x44;
        typeIdsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x48;
        protoIdsSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x4C;
        protoIdsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x50;
        fieldIdsSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x54;
        fieldIdsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x58;
        methodIdsSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x5C;
        methodIdsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x60;
        classDefsSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x64;
        classDefsOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x68;
        dataSize =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
        startIndex = 0x6C;
        dataOff =  Util.bytes2int(Arrays.copyOfRange(fileDate, startIndex, startIndex+4));
    }


    public void show(){
        System.out.println("Dex header");
        StringBuilder sb = new StringBuilder();
        for(byte b: magic){
            sb.append(String.format("%02X ", b));
        }
        System.out.println("  magic: "+sb.toString());
        System.out.println("  checksum: "+String.format("%04X ", checkSum));

        sb = new StringBuilder();
        for(byte b: signature){
            sb.append(String.format("%02X ", b));
        }
        System.out.println("  signature: "+sb.toString());
        System.out.printf("  fileSize: 0x%X\n", fileSize);
        System.out.printf("  headerSize: 0x%X\n", headerSize);
        System.out.printf("  endianTag: 0x%X\n", endianTag);
        System.out.printf("  linkSize: 0x%X\n", linkSize);
        System.out.printf("  linkOff: 0x%X\n", linkOff);
        System.out.printf("  mapOff: 0x%X\n", mapOff);
        System.out.printf("  stringIdsSize: 0x%X\n", stringIdsSize);
        System.out.printf("  stringIdsOff: 0x%X\n", stringIdsOff);

        System.out.printf("  typeIdsSize: 0x%X\n", typeIdsSIze);
        System.out.printf("  typeIdsOff: 0x%X\n", typeIdsOff);

        System.out.printf("  protoIdsSize: 0x%X\n", protoIdsSize);
        System.out.printf("  protoIdsOff: 0x%X\n", protoIdsOff);

        System.out.printf("  fieldIdsSize: 0x%X\n", fieldIdsSize);
        System.out.printf("  fieldIdsOff: 0x%X\n", fieldIdsOff);

        System.out.printf("  methodIdsSize: 0x%X\n", methodIdsSize);
        System.out.printf("  methodIdsOff: 0x%X\n", methodIdsOff);

        System.out.printf("  classDefsSize: 0x%X\n", classDefsSize);
        System.out.printf("  classDefsOff: 0x%X\n", classDefsOff);

        System.out.printf("  dataSize: 0x%X\n", dataSize);
        System.out.printf("  dataOff: 0x%X\n", dataOff);

    }
}
