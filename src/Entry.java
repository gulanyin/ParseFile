import arsc.ArscFile;
import dex.DexFile;
import elf.ElfFile;
import xml.AndroidManifestFile;


public class Entry {



    public static void main(String[] args) throws Exception {
        if(args.length < 2){
            System.out.println("-elf  elfFilePath");
            System.out.println("-dex  dexFilePath");
            System.out.println("-arsc  arscFilePath");
            System.out.println("-manifest  manifestFilePath");
        }else{
            String type = args[0];
            String filePath = args[1];
            if("-elf".equalsIgnoreCase(type)){
                ElfFile elfFile = new ElfFile(filePath);
                elfFile.show();
            }else if("-dex".equalsIgnoreCase(type)){
                DexFile dexFile = new DexFile(filePath);
                dexFile.show();
            }else if("-arsc".equalsIgnoreCase(type)){
                ArscFile arscFile = new ArscFile(filePath);
                arscFile.show();
            }else if("-manifest".equalsIgnoreCase(type)){
                AndroidManifestFile file = new AndroidManifestFile(filePath);
                file.show();
            }

        }
    }

}
