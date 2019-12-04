package dexclass;

import dex.DexCode;
import lombok.Data;

@Data
public class DexMethod {
    private int methodIdx;
    private int accessFlags;
    private int codeOff;
    private DexCode dexCode;

    public void init(byte[] fileData) {
        if(codeOff != 0){
            dexCode = new DexCode();
            dexCode.init(fileData, codeOff);
        }
    }
}
