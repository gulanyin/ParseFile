package elf;

import common.Util;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Data
public class Elf32_Sym {
    private static HashMap<Integer, String> bindMap = new HashMap();
    private static HashMap<Integer, String> typeMap = new HashMap();
    static {
        // bindMap prifix = "STB_" LOCAL, GLOBAL
        bindMap.put(0, "LOCAL");
        bindMap.put(1, "GLOBAL");
        bindMap.put(2, "WEAK");
        bindMap.put(3, "NUM");
        bindMap.put(13, "LOPROC");
        bindMap.put(15, "HIPROC");

        // typeMap prifix = "STT_" NOTYPE, OBJECT
        typeMap.put(0, "NOTYPE");
        typeMap.put(1, "OBJECT");
        typeMap.put(2, "FUNC");
        typeMap.put(3, "SECTION");
        typeMap.put(4, "FILE");
        typeMap.put(5, "NUM");
        typeMap.put(13, "LOPROC");
        typeMap.put(15, "HIPROC");
    }

    private int st_name;
    private int st_value;
    private int st_size;
    private byte st_info;
    private byte st_other;
    private short st_shndx;

    private String type;
    private String bind;
//    private String symbolName;

    public void init(byte[] fileData, int startIndex){
        st_name = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*0, 4));
        st_value = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*1, 4));
        st_size = Util.bytes2int(Util.copyOfRange(fileData, startIndex+0x4*2, 4));
        st_info = fileData[startIndex+0x4*3];
        st_other = fileData[startIndex+0x4*3+1];
        st_shndx = Util.bytes2short(Util.copyOfRange(fileData, startIndex+0x4*3+2, 2));
        bind = bindMap.get((st_info & 0x00F0)>> 4);
        type = typeMap.get((st_info & 0x000F));
    }

    public String show(byte[] dynStrName) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(
                String.format("%08X %08X %08X %08X %08X %08X %-8s %-8s %s",
                        st_name, st_value, st_size, st_info, st_other, st_shndx, bind, type, Util.readCString(dynStrName, st_name)
                ));

        return sb.toString();
    }
}
