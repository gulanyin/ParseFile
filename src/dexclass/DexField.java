package dexclass;

import lombok.Data;

@Data
public class DexField {
    private int fieldIdx;
    private int accessFlags;

    public String show(){
        return String.format("fieldIdx: %d   accessFlags: %d", fieldIdx, accessFlags);
    }
}
