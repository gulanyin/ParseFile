package xml;

import common.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;

public class AndroidManifestFile {
    private byte[] originFileData;
    private ResChunkHeader header;
    private StringChunkHeader stringChunkHeader;
    private ResourceIdChunkHeader resourceIdChunkHeader;

    public AndroidManifestFile(String dexFilePath) throws Exception{
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

    private void parse(){
        this.initHeader();
        this.initStringChunkHeader();
        this.initResourceIdChunkHeader();
    }

    private void initHeader(){
        header = new ResChunkHeader();
        header.init(originFileData);
    }

    private void initStringChunkHeader(){
        stringChunkHeader = new StringChunkHeader();
        stringChunkHeader.init(originFileData, header.getHeardSize());
    }

    private void initResourceIdChunkHeader(){
        resourceIdChunkHeader = new ResourceIdChunkHeader();
        resourceIdChunkHeader.init(originFileData, header.getHeardSize() + stringChunkHeader.getChunkSize());
    }


    public void show() throws UnsupportedEncodingException {
        this.showHeader();
        this.showStringChunkHeader();
        this.showResourceIdChunkHeader();
        this.showAllXmlContentChunk();
    }

    private void showHeader() {
        System.out.print("header: ");
        System.out.printf("\n  type: %X", header.getType());
        System.out.printf("\n  headerSize: %d(0x%X)", header.getHeardSize(), header.getHeardSize());
        System.out.printf("\n  size: %d(0x%X)", header.getSize(), header.getSize());
    }

    private void showStringChunkHeader() throws UnsupportedEncodingException {
        System.out.print("\nStringChunkHeader: ");
        System.out.printf("\n  chunkType: 0x%X", stringChunkHeader.getChunkType());
        System.out.printf("\n  chunkSize: 0x%X", stringChunkHeader.getChunkSize());
        System.out.printf("\n  stringCount: 0x%X", stringChunkHeader.getStringCount());
        System.out.printf("\n  styleCount: 0x%X", stringChunkHeader.getStyleCount());
        System.out.printf("\n  unKnow: 0x%X", stringChunkHeader.getUnKnow());
        System.out.printf("\n  stringStart: 0x%X", stringChunkHeader.getStringStart());
        System.out.printf("\n  styleStart: 0x%X", stringChunkHeader.getStyleStart());
        System.out.printf("\n  stringPool:");
        for(int i=0; i<stringChunkHeader.getStringCount(); i++){
            System.out.printf("\n       [%3d]: %s", i, stringChunkHeader.getStringByIndex(originFileData, i));
        }
        System.out.printf("\n  stylePool:");
        for(int i=0; i<stringChunkHeader.getStyleCount(); i++){
            System.out.printf("\n       [%3d]: %s", i, stringChunkHeader.getStyleByIndex(originFileData, i));
        }

    }

    private void showResourceIdChunkHeader(){
        System.out.print("\nResourceIdChunkHeader: ");
        System.out.printf("\n  chunkType: 0x%X", resourceIdChunkHeader.getChunkType());
        System.out.printf("\n  chunkSize: 0x%X", resourceIdChunkHeader.getChunkSize());
        System.out.print("\n  resourceIds:");
        for(int i=0; i< resourceIdChunkHeader.getResourceId().length; i++){
            System.out.printf("\n       [%3d]: 0x%08X", i, resourceIdChunkHeader.getResourceId()[i]);
        }
    }

    private XmlContent getXmlContentChunk(int startOffset){
        int chunkType = Util.bytes2int(Util.copyOfRange(originFileData, startOffset, 4));
        switch (chunkType){
            case XmlConstant.START_NAMESPACE_CHUNK:
                return new XmlNamespaceChunk();
            case XmlConstant.END_NAMESPACE_CHUNK:
                return new XmlNamespaceChunk();
            case XmlConstant.START_TAG_CHUNK:
                return new XmlStartTagChunk();
            case XmlConstant.END_TAG_CHUNK:
                return new XmlEndTagChunk();
            default:
                throw new RuntimeException("error chunkType "+ chunkType);
        }
    }

    private void showAllXmlContentChunk() throws UnsupportedEncodingException {
        int startOffset = header.getHeardSize() + stringChunkHeader.getChunkSize() + resourceIdChunkHeader.getChunkSize();
        while(startOffset < originFileData.length){
            XmlContent xmlContent = getXmlContentChunk(startOffset);
            xmlContent.init(originFileData, startOffset);
            xmlContent.show(originFileData, stringChunkHeader);
            startOffset += xmlContent.getChunkSize();
        }

    }

}
