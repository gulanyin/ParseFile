package elf;

import common.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElfFile {
    private byte[] originFileData;
    private byte[] sectionStrName;
    private int maxSectionStrName = 0;
    private byte[] dynStrName;
    private int maxDynStrName = 0;
    private Elf32_Ehdr elf32_ehdr;
    private List<Elf32_Shdr> elf32_shdrsArrayList = new ArrayList<>();
    private List<Elf32_Sym> elf32_symsArrayList = new ArrayList<>();

    public ElfFile(String dexFilePath) throws Exception{
        File dexFile = new File(dexFilePath);
        if(!dexFile.exists()){
            throw new Exception(dexFilePath + "not exist");
        }
        originFileData = new byte[(int)dexFile.length()];
        new FileInputStream(dexFile).read(originFileData);
        System.out.println("start parse...");
        this.parse();
        System.out.println("parse end");
    }

    private void parse() throws UnsupportedEncodingException {
        this.initElf32_ehdr();
        this.initElf32_shdrsArrayList();
        this.initElf32_symsArrayList();
    }

    private void initElf32_ehdr(){
        elf32_ehdr = new Elf32_Ehdr();
        elf32_ehdr.init(originFileData);
    }

    private void initElf32_shdrsArrayList() throws UnsupportedEncodingException {
        for(int i=0; i<elf32_ehdr.getE_shnum(); i++){
            int offset = elf32_ehdr.getE_shoff() + i* elf32_ehdr.getE_shentsize();
            Elf32_Shdr elf32_shdr = new Elf32_Shdr();
            elf32_shdr.init(originFileData, offset);
            elf32_shdrsArrayList.add(elf32_shdr);
        }

        // 节头表名称字符串表，字节数组
        Elf32_Shdr elf32_shdr = elf32_shdrsArrayList.get(elf32_ehdr.getE_shstrndx());
        int offset = elf32_shdr.getSh_offset();
        int size = elf32_shdr.getSh_size();
        sectionStrName = Arrays.copyOfRange(originFileData, offset, offset+size);
        for(Elf32_Shdr temp: elf32_shdrsArrayList){
            String sectionName = Util.readCString(sectionStrName, temp.getSh_name());
            // 最长的节名， 用作格式打印
            if(maxSectionStrName < sectionName.length()){
                maxSectionStrName = sectionName.length();
            }
        }
    }

    private void initElf32_symsArrayList() throws UnsupportedEncodingException {
        for(Elf32_Shdr shdr: elf32_shdrsArrayList){
            String sectionName = Util.readCString(sectionStrName, shdr.getSh_name());
            if(sectionName.equalsIgnoreCase(".dynstr") && shdr.getSh_type() == 0x3){
                // 求得符号名称字符串表
                int offset = shdr.getSh_offset();
                int size = shdr.getSh_size();
                dynStrName = Arrays.copyOfRange(originFileData, offset, offset+size);
            }

            if(sectionName.equalsIgnoreCase(".dynsym")){
                // 符号表
                int numberOfDym = shdr.getSh_size() / shdr.getSh_entsize();
                for(int i=0; i<numberOfDym; i++){
                    int offset = shdr.getSh_offset() + i*shdr.getSh_entsize();
                    Elf32_Sym elf32_sym = new Elf32_Sym();
                    elf32_sym.init(originFileData, offset);
                    elf32_symsArrayList.add(elf32_sym);
                }
            }
        }

        // 符号加载完毕， 求最长的符号名
//        for(Elf32_Sym elf32_sym: elf32_symsArrayList){
//            String symName = Util.readCString(dynStrName, elf32_sym.getSt_name());
//            if(maxDynStrName < symName.length()){
//                maxDynStrName = symName.length();
//            }
//        }
    }


    public void show() throws UnsupportedEncodingException {
        this.showElf32_ehdr();
        this.showSections();
        this.showSegments();
        this.showSymbols();
        this.showRel();

    }

    private void showElf32_ehdr(){
        System.out.println("elf header: ");
        System.out.println(elf32_ehdr.show("  " ));
    }

    private void showSections() throws UnsupportedEncodingException {
        System.out.print("sections : ");
        System.out.printf("\n  [   ] %-"+maxSectionStrName+"s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-12s %-10s",
                "name", "sh_name", "sh_type", "sh_flags", "sh_addr", "sh_offset", "sh_size", "sh_link", "sh_info", "sh_addralign", "sh_entsize");
        for(int i=0; i<elf32_shdrsArrayList.size(); i++){
            Elf32_Shdr elf32_shdr = elf32_shdrsArrayList.get(i);
            System.out.printf("\n  [%3d] %s", i, elf32_shdr.show(sectionStrName, maxSectionStrName));
        }
    }


    private void showSymbols() throws UnsupportedEncodingException {
        System.out.print("\nsymbols : ");
        int numWidth = (elf32_symsArrayList.size()+"").length();
        String numColWidth = String.format("[%%%ds]", numWidth);
        System.out.printf("\n  "+numColWidth+" %-8s %-8s %-8s %-8s %-8s %-8s %-8s %-8s %s",
                "", "name", "value", "size", "info", "other", "shndx",  "bind", "type", "strname");
        for(int i=0; i<elf32_symsArrayList.size(); i++){
            Elf32_Sym elf32_sym = elf32_symsArrayList.get(i);
            System.out.printf("\n  "+numColWidth+" %s", i, elf32_sym.show(dynStrName));
        }

    }

    private void showRel() throws UnsupportedEncodingException {
        System.out.print("\nrels : ");
        List<Elf32_Rel> relList = new ArrayList<>();
        for(Elf32_Shdr elf32Shdr : elf32_shdrsArrayList){
            if(elf32Shdr.getSh_type() == 0x9){  // 0x9 是 SHT_REL 类型的
                int numberOfRel = elf32Shdr.getSh_size() / elf32Shdr.getSh_entsize(); // 从定位项个数
                for(int i=0; i< numberOfRel; i++){
                    Elf32_Rel elf32Rel = new Elf32_Rel();
                    int offset = elf32Shdr.getSh_offset() + i*elf32Shdr.getSh_entsize(); // 每个重定位项偏移
                    elf32Rel.init(originFileData, offset);
                    relList.add(elf32Rel);
                }
            }
        }

        // 打印重定位项
        int numWidth = (relList.size()+"").length();
        String numColWidth = String.format("[%%%ds]", numWidth);
        System.out.printf("\n  "+numColWidth+" %-8s %-8s %-8s %s",
                "", "offset", "info", "Sym.Val", "Sym.Name");
        for(int i=0; i<relList.size(); i++){
            StringBuilder sb = new StringBuilder();
            Elf32_Rel elf32Rel = relList.get(i);
            sb.append(String.format("\n  "+numColWidth+" %08X %08X ", i, elf32Rel.getR_offset(), elf32Rel.getR_info()));
            int symbolIndex = elf32Rel.getR_info() >> 8; // 低8位是重定位类型，高24位是符号表的index
            if(symbolIndex !=0){  //
                Elf32_Sym elf32Sym = elf32_symsArrayList.get(symbolIndex);
                sb.append(String.format("%08X %s", elf32Sym.getSt_value(), Util.readCString(dynStrName, elf32Sym.getSt_name())  ));
            }
            System.out.print(sb.toString());
        }
    }

    private void showSegments(){
        System.out.print("\nSegments : ");
        List<Elf32_Phdr> phdrList = new ArrayList<>();
        for(int i=0; i<elf32_ehdr.getE_phnum(); i++){
            int offset = elf32_ehdr.getE_phoff() + i*elf32_ehdr.getE_phentsize();
            Elf32_Phdr phdr = new Elf32_Phdr();
            phdr.init(originFileData, offset);
            phdrList.add(phdr);
        }

        // 打印程序头部表
        int numWidth = (phdrList.size()+"").length();
        String numColWidth = String.format("[%%%ds]", numWidth);
        System.out.printf("\n  "+numColWidth+" %-18s %-8s %-8s %-8s %-8s %-8s %-8s %-8s",
                "", "type", "offset", "vaddr", "paddr", "filesz", "memsz", "flags", "align");

        for(int i=0; i<phdrList.size(); i++){
            Elf32_Phdr elf32Phdr = phdrList.get(i);
            System.out.printf(String.format("\n  "+numColWidth+" %s", i, elf32Phdr.show() ));
        }
    }
}
