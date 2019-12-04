package dex;

import common.Util;
import lombok.Data;

@Data
public class DexProtoId {
    private int shortyIdx;
    private int returnTypeIdx;
    private int parametersOff;

    public void init(byte[] fileDate, int startIndex){
        shortyIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex, 4));
        returnTypeIdx = Util.bytes2int(Util.copyOfRange(fileDate, startIndex+4, 4));
        parametersOff = Util.bytes2int(Util.copyOfRange(fileDate, startIndex+8, 4));
    }

    public DexTypeList parametersDexTypeList(byte[] fileDate){
        if(parametersOff == 0){
            return null;
        }
        DexTypeList dexTypeList = new DexTypeList();
        dexTypeList.init(fileDate, parametersOff);
        return dexTypeList;
    }
}
