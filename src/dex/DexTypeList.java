package dex;

import common.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DexTypeList {
    private int size;
    private List<DexTypeItem> list = new ArrayList<>();

    public void init(byte[] fileDate, int startIndex){
        size = Util.bytes2int(Util.copyOfRange(fileDate, startIndex, 4));
        for(int i=0; i<size; i++){
            int offset = startIndex + 4+ i*2;  // DexTypeItem 是2字节
            DexTypeItem dexTypeItem = new DexTypeItem();
            short typeIdx = Util.bytes2short(Util.copyOfRange(fileDate, offset, 2));
            dexTypeItem.setTypeIdx(typeIdx);
            list.add(dexTypeItem);
        }
    }
}
