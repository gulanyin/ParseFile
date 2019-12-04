package dex;

import common.Util;
import lombok.Data;

@Data
public class DexCode {
    private short registersSize;   // 0x0
    private short insSize;   // 0x2
    private short outsSize;  // 0x4
    private short triesSize;  // 0x6
    private int debugInfoOff; // 0x8
    private int insnsSize;  // 0xc
    private byte[] insns; // 0x10

    private int codeOff;

    public void init(byte[] fileData, int codeOff) {
        registersSize = Util.bytes2short(Util.copyOfRange(fileData, codeOff, 2));
        insSize = Util.bytes2short(Util.copyOfRange(fileData, codeOff+0x2, 2));
        outsSize = Util.bytes2short(Util.copyOfRange(fileData, codeOff+0x4, 2));
        triesSize = Util.bytes2short(Util.copyOfRange(fileData, codeOff+0x6, 2));
        debugInfoOff = Util.bytes2int(Util.copyOfRange(fileData, codeOff+0x8, 4));
        insnsSize = Util.bytes2int(Util.copyOfRange(fileData, codeOff+0xC, 4));
//        insns = new byte[insnsSize * 2];
        this.codeOff = codeOff;
//        insns = Util.copyOfRange(fileData, codeOff+0x10, insnsSize * 2);
//        for(int i=0; i<insnsSize; i++){
//            insns[i] = Util.bytes2short(Util.copyOfRange(fileData, codeOff+0x10 + i*2, 2));
//        }

    }

    public String show(byte[] originFileData){
        StringBuilder sb = new StringBuilder();
        byte[] code = Util.copyOfRange(originFileData, codeOff+0x10, insnsSize * 2);
        for(byte b : code ){
            sb.append(String.format("%02X ", b));
        }

        return String.format("registersSize: %d  insSize: %d  outsSize: %d triesSize: %d debugInfoOff: 0x%X insnsSize: %d  openCode: %s",
                registersSize, insSize, outsSize, triesSize, debugInfoOff, insnsSize, sb.toString());
    }
}
