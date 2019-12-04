package arsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

public class ArscFile {
    private byte[] originFileData;
    private ResTable_header resTable_header;
    private ResStringPool_header resStringPool_header;
    private ResTable_package resTable_package;

    public ArscFile(String dexFilePath) throws Exception{
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
        this.initResTable_header();
        this.initResStringPool_header();
        this.initResTable_package();
    }

    private void initResTable_header(){
        resTable_header = new ResTable_header();
        resTable_header.init(originFileData, 0);
    }

    private void initResStringPool_header(){
        resStringPool_header = new ResStringPool_header();
        resStringPool_header.init(originFileData, resTable_header.getResChunk_header().getHeaderSize());
    }

    private void initResTable_package() throws UnsupportedEncodingException {
        resTable_package = new ResTable_package();
        resTable_package.init(originFileData,
                resTable_header.getResChunk_header().getHeaderSize() + resStringPool_header.getResChunk_header().getSize());
    }

    public void show() throws UnsupportedEncodingException {
        this.showResTable_header();
        this.showResStringPool();
        this.showResTablePackage();
    }

    private void showResTable_header(){
        resTable_header.show();
    }

    private void showResStringPool() throws UnsupportedEncodingException {
        resStringPool_header.show(originFileData, resTable_header);
    }

    private void showResTablePackage() throws UnsupportedEncodingException {
        int startIndex = resTable_header.getResChunk_header().getHeaderSize() + resStringPool_header.getResChunk_header().getSize();
        resTable_package.show(originFileData, startIndex);
    }
}
