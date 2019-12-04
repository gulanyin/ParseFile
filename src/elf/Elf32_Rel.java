package elf;

import common.Util;
import lombok.Data;

@Data
public class Elf32_Rel {
    private int r_offset;
    private int r_info;

    public void init(byte[] fileData, int startIndex){
        r_offset = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        r_info = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
    }

}