package dex;

import common.Util;
import lombok.Data;

@Data
public class DexClassDef {
    private int classIdx;
    private int accessFlags;
    private int superclassIdx;
    private int interfacesOff;
    private int sourceFileIdx;
    private int annotationsOff;
    private int classDataOff;
    private int staticValuesOff;

    public void init(byte[] fileDate, int startIndex){
        classIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 0*4, 4));
        accessFlags = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 1*4, 4));
        superclassIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 2*4, 4));
        interfacesOff = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 3*4, 4));
        sourceFileIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 4*4, 4));
        annotationsOff = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 5*4, 4));
        classDataOff = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 6*4, 4));
        staticValuesOff = Util.bytes2int(Util.copyOfRange(fileDate, startIndex + 7*4, 4));
    }

}
