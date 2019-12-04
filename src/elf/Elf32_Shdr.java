package elf;

import common.Util;
import lombok.Data;

import java.io.UnsupportedEncodingException;

@Data
public class Elf32_Shdr {
    private int sh_name;
    private int sh_type;
    private int sh_flags;
    private int sh_addr;
    private int sh_offset;
    private int sh_size;
    private int sh_link;
    private int sh_info;
    private int sh_addralign;
    private int sh_entsize;

    public void init(byte[] fileData, int startIndex){
        sh_name = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*0, 4));
        sh_type = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*1, 4));
        sh_flags = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*2, 4));
        sh_addr = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*3, 4));
        sh_offset = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*4, 4));
        sh_size = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*5, 4));
        sh_link = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*6, 4));
        sh_info = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*7, 4));
        sh_addralign = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*8, 4));
        sh_entsize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*9, 4));
    }

    public String show(byte[] sectionStrName, int maxSectionStrName) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(
                String.format("%-"+maxSectionStrName+"s %08X   %08X   %08X   %08X   %08X   %08X   %08X   %08X   %08X     %08X",
                        Util.readCString(sectionStrName, sh_name),
                        sh_name, sh_type, sh_flags, sh_addr, sh_offset, sh_size, sh_link, sh_info, sh_addralign, sh_entsize
                ));

        return sb.toString();
    }
}
