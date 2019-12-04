package xml;

import lombok.Data;

import java.io.UnsupportedEncodingException;

@Data
public class XmlContent {

    public int chunkType;
    public int chunkSize;
    public int lineNumber;

    public String getChunkTypeName(){
        String name = "";
        switch (chunkType){
            case XmlConstant.START_NAMESPACE_CHUNK:
                name = "START_NAMESPACE_CHUNK";
                break;
            case XmlConstant.END_NAMESPACE_CHUNK:
                name = "END_NAMESPACE_CHUNK";
                break;
            case XmlConstant.START_TAG_CHUNK:
                name = "START_TAG_CHUNK";
                 break;
            case XmlConstant.END_TAG_CHUNK:
                name = "END_TAG_CHUNK";
                break;
            default:
                name = "";
        }
        return name;
    }

    public void init(byte[] fileData, int startIndex) {
        throw new RuntimeException(" init must be implements");
    }

    public void show( byte[] fileData, StringChunkHeader stringChunkHeader) throws UnsupportedEncodingException {
        throw new RuntimeException(" init must be implements");
    }

}
