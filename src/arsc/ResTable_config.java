package arsc;

import common.Util;
import lombok.Data;

@Data
public class ResTable_config {
    private int size;
    /* union {
        struct {
            uint16_t mcc; // Mobile country code (from SIM).  0 means "any".
            uint16_t mnc; // Mobile network code (from SIM).  0 means "any".
        };
        uint32_t imsi;
    };
    */
    private short mcc;
    private short mnc;
    private int imsi;

    /* union {
            struct {
                char language[2];
                 char country[2];
            };
            uint32_t locale;
        };
        */
    private byte []language; // = new byte[2];
    private byte []country; // = new byte[2];
    private int locale;

    public void init(byte[] fileData, int startIndex){
        size = Util.bytes2int(Util.copyOfRange(fileData, startIndex, 4));
        imsi = Util.bytes2int(Util.copyOfRange(fileData, startIndex+4, 4));
        mcc = Util.bytes2short(Util.copyOfRange(fileData, startIndex+4, 2));
        mnc = Util.bytes2short(Util.copyOfRange(fileData, startIndex+4+2, 2));

        locale = Util.bytes2int(Util.copyOfRange(fileData, startIndex+8, 4));
        language = Util.copyOfRange(fileData, startIndex+8, 2);
        country = Util.copyOfRange(fileData, startIndex+8+2, 2);
    }

}
