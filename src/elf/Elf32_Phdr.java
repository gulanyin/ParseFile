package elf;

import common.Util;
import lombok.Data;

import java.util.HashMap;

@Data
public class Elf32_Phdr {
    private static HashMap<Integer, String> typeMap = new HashMap<>();
    static {
        // prefix = "PT_"  PT_NULL.PT_LOAD ...
        typeMap.put(0, "NULL");
        typeMap.put(1, "LOAD");
        typeMap.put(2, "DYNAMIC");
        typeMap.put(3, "INTERP");
        typeMap.put(4, "NOTE");
        typeMap.put(5, "SHLIB");
        typeMap.put(6, "PHDR");
        typeMap.put(7, "NUM");
        typeMap.put(0x60000000, "LOOS");
        typeMap.put(0x6fffffff, "HIOS");
        typeMap.put(0x70000000, "LOPROC");
        typeMap.put(0x7fffffff, "HIPROC");
    }

    private int p_type;
    private int p_offset;
    private int p_vaddr;
    private int p_paddr;
    private int p_filesz;
    private int p_memsz;
    private int p_flags;
    private int p_align;

    private String p_typeName;


    public void init(byte[] fileData, int startIndex){
        p_type = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*0, 4));
        p_typeName = typeMap.get(p_type);
        p_offset = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*1, 4));
        p_vaddr = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*2, 4));
        p_paddr = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*3, 4));
        p_filesz = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*4, 4));
        p_memsz = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*5, 4));
        p_flags = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*6, 4));
        p_align = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*7, 4));
    }

    public String show(){
        return  String.format("%08X(%-8s) %08X %08X %08X %08X %08X %08X %08X",
                p_type, p_typeName, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, p_flags, p_align);
    }
}
