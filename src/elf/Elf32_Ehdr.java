package elf;

import common.Util;
import lombok.Data;

@Data
public class Elf32_Ehdr {
    private byte[] e_ident;
    private short e_type;
    private short e_machine;
    private int e_version;

    private int e_entry;
    private int e_phoff;
    private int e_shoff;
    private int e_flags;
    private short e_ehsize;

    private short e_phentsize;
    private short e_phnum;
    private short e_shentsize;
    private short e_shnum;

    private short e_shstrndx;

    public void init(byte[] fileData ){
        e_ident = Util.copyOfRange(fileData, 0x0, 0x10);
        e_type = Util.bytes2short(Util.copyOfRange(fileData, 0x10, 2));
        e_machine = Util.bytes2short(Util.copyOfRange(fileData, 0x12, 2));
        e_version = Util.bytes2int(Util.copyOfRange(fileData, 0x14, 4));


        e_entry = Util.bytes2int(Util.copyOfRange(fileData, 0x18, 4));
        e_phoff = Util.bytes2int(Util.copyOfRange(fileData, 0x1C, 4));
        e_shoff = Util.bytes2int(Util.copyOfRange(fileData, 0x20, 4));
        e_flags = Util.bytes2int(Util.copyOfRange(fileData, 0x24, 4));
        e_ehsize = Util.bytes2short(Util.copyOfRange(fileData, 0x28, 2));

        e_phentsize = Util.bytes2short(Util.copyOfRange(fileData, 0x2A, 2));
        e_phnum = Util.bytes2short(Util.copyOfRange(fileData, 0x2C, 2));
        e_shentsize = Util.bytes2short(Util.copyOfRange(fileData, 0x2E, 2));
        e_shnum = Util.bytes2short(Util.copyOfRange(fileData, 0x30, 2));

        e_shstrndx = Util.bytes2short(Util.copyOfRange(fileData, 0x32, 2));
    }

    public String show(String rowStart){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s e_ident: %s", rowStart, Util.byte2HexString(e_ident)));
        sb.append(String.format("\n%s e_type: 0x%X", rowStart, e_type));
        sb.append(String.format("\n%s e_machine: %d", rowStart, e_machine));
        sb.append(String.format("\n%s e_version: 0x%X", rowStart, e_version));
        sb.append(String.format("\n%s e_entry: 0x%X", rowStart, e_entry));
        sb.append(String.format("\n%s e_phoff: 0x%X", rowStart, e_phoff));
        sb.append(String.format("\n%s e_shoff: 0x%X", rowStart, e_shoff));
        sb.append(String.format("\n%s e_flags: 0x%X", rowStart, e_flags));
        sb.append(String.format("\n%s e_ehsize: 0x%X", rowStart, e_ehsize));
        sb.append(String.format("\n%s e_phentsize: 0x%X", rowStart, e_phentsize));
        sb.append(String.format("\n%s e_phnum: %d", rowStart, e_phnum));
        sb.append(String.format("\n%s e_shentsize: 0x%X", rowStart, e_shentsize));
        sb.append(String.format("\n%s e_shnum: %d", rowStart, e_shnum));
        sb.append(String.format("\n%s e_shstrndx: %d", rowStart, e_shstrndx));
        return sb.toString();
    }

}
