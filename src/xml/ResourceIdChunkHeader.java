package xml;

import common.Util;
import lombok.Data;

@Data
public class ResourceIdChunkHeader {

    private int chunkType;
    private int chunkSize;
    private int[] resourceId;

    public void init(byte[] fileData, int startIndex){
        chunkType = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        chunkSize = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        int numberOfids = (chunkSize - 8)/4;
        resourceId = new int[numberOfids];
        for(int i=0; i<numberOfids; i++){
            resourceId[i] = Util.bytes2int(Util.copyOfRange(fileData, startIndex+8+ i*4, 4));
        }
    }

}
