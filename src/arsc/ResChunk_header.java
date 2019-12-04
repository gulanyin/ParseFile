package arsc;

import common.Util;
import lombok.Data;

@Data
public class ResChunk_header {
    public static final int RES_NULL_TYPE               = 0x0000;
    public static final int RES_STRING_POOL_TYPE        = 0x0001;
    public static final int RES_TABLE_TYPE              = 0x0002;
    public static final int RES_XML_TYPE                = 0x0003;
    // Chunk types in RES_XML_TYPE
    public static final int RES_XML_FIRST_CHUNK_TYPE    = 0x0100;
    public static final int RES_XML_START_NAMESPACE_TYPE= 0x0100;
    public static final int RES_XML_END_NAMESPACE_TYPE  = 0x0101;
    public static final int RES_XML_START_ELEMENT_TYPE  = 0x0102;
    public static final int RES_XML_END_ELEMENT_TYPE    = 0x0103;
    public static final int RES_XML_CDATA_TYPE          = 0x0104;
    public static final int RES_XML_LAST_CHUNK_TYPE     = 0x017f;
    // This contains a uint32_t array mapping strings in the string
    // pool back to resource identifiers.  It is optional.
    public static final int RES_XML_RESOURCE_MAP_TYPE   = 0x0180;
    // Chunk types in RES_TABLE_TYPE
    public static final int RES_TABLE_PACKAGE_TYPE      = 0x0200;
    public static final int RES_TABLE_TYPE_TYPE         = 0x0201;
    public static final int RES_TABLE_TYPE_SPEC_TYPE    = 0x0202;
    public static final int RES_TABLE_LIBRARY_TYPE      = 0x0203;


    private short type;
    private short headerSize;
    private int size;

    public void init(byte[] fileData, int startIndex){
        type = Util.bytes2short(Util.copyOfRange(fileData, startIndex, 2));
        headerSize = Util.bytes2short(Util.copyOfRange(fileData, startIndex+2, 2));
        size = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
    }
}
