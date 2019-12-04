package common;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Util {
    public static byte[] int2byteArray(int i){
        byte[] bArray=new byte[4];
        bArray[0]=(byte) i;
        bArray[1]=(byte) (i>>8);
        bArray[2]=(byte) (i>>16);
        bArray[3]=(byte) (i>>24);
        return bArray;
    }

    public static int bytes2int(byte[]  bytes){
        int h1 = bytes[0] & 0xFF;
        int h2 = (bytes[1] & 0xFF) << 8;
        int h3 = (bytes[2] & 0xFF) << 16;
        int h4 = (bytes[3] & 0xFF) << 24;
        return h4 | h3 | h2 | h1;

    }

    public static short bytes2short(byte[]  bytes){
        int h1 = bytes[0] & 0xFF;
        int h2 = (bytes[1] & 0xFF) << 8;
        return (short) (h2 | h1);

    }

    public static String bytes2String(byte[] bytes){
        try {
            return new String(bytes, "ascii");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] copyOfRange(byte[] originData, int from, int length){
        return Arrays.copyOfRange(originData, from, from+length);
    }

    public static byte[] readULeb128(byte[] data, int startIndex){
        List<Byte> byteAryList = new ArrayList<Byte>();

        int result = (int)data[startIndex];
        byteAryList.add(data[startIndex]);

        if(result > 0x7f){
            int cur = data[startIndex+1];
            byteAryList.add(data[startIndex+1]);
            if(cur > 0x7f){
                cur = data[startIndex+2];
                byteAryList.add(data[startIndex+2]);
                if(cur > 0x7f){
                    cur = data[startIndex+3];
                    byteAryList.add(data[startIndex+3]);
                    if(cur > 0x7f){
                        cur = data[startIndex+4];
                        byteAryList.add(data[startIndex+4]);
                    }
                }
            }
        }

        byte[] byteAry = new byte[byteAryList.size()];
        for(int j=0;j<byteAryList.size();j++){
            byteAry[j] = byteAryList.get(j);
        }
        return byteAry;
    }

    public static int readULeb128(byte[] data){
        int startIndex = 0;
        int result = (int)data[startIndex];

        if(result > 0x7f){
            int cur = data[startIndex+1];
            result = (result & 0x7f) | ((cur & 0x7f) << 7);
            if(cur > 0x7f){
                cur = data[startIndex+2];
                result |= (cur & 0x7f) << 14;

                if(cur > 0x7f){
                    cur = data[startIndex+3];
                    result |= (cur & 0x7f) << 21;

                    if(cur > 0x7f){
                        cur = data[startIndex+4];
                        result |= (cur & 0x7f) << 28;
                    }
                }
            }
        }
        return result;
    }

    public static Map<String, Integer> readMapULeb128(byte[] data, int startIndex){
        int result = 0xff & data[startIndex];
        int nextIndex = startIndex+1;

        if(result > 0x7f){
            int cur = 0xff & data[startIndex+1];
            result = (result & 0x7f) | ((cur & 0x7f) << 7);
            nextIndex ++;
            if(cur > 0x7f){
                cur = 0xff & data[startIndex+2];
                result |= (cur & 0x7f) << 14;
                nextIndex ++;
                if(cur > 0x7f){
                    cur = 0xff & data[startIndex+3];
                    result |= (cur & 0x7f) << 21;
                    nextIndex ++;
                    if(cur > 0x7f){
                        cur = 0xff & data[startIndex+4];
                        result |= (cur & 0x7f) << 28;
                        nextIndex ++;
                    }
                }
            }
        }

        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put("result", result);
        resultMap.put("nextIndex", nextIndex);

        return resultMap;
    }


    public static String byte2HexString(byte[] data){
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b: data){
            stringBuilder.append(String.format("%02X ", b));
        }
        return stringBuilder.toString();
    }

    public static String readCString(byte[] data, int offset) throws UnsupportedEncodingException {
        int endIndex = offset;
        while(data[endIndex] != 0){
            endIndex ++;
        }

        if(endIndex == offset){
            return "";
        }else{
            return new String(Arrays.copyOfRange(data, offset, endIndex), "ASCII");
        }

    }


    public static String readU16lenString(byte[] fileData, int stringFileOffset) throws UnsupportedEncodingException {
        short stringLength = bytes2short(copyOfRange(fileData, stringFileOffset, 2));  // 开始两个字节是字符串长度
        byte[] stringContent = copyOfRange(fileData, stringFileOffset+2, stringLength*2) ;
        List<Byte> byteList = new ArrayList<>();
        for(byte b: stringContent){
            if(b !=0x0){
                byteList.add(b);
            }
        }
        stringContent = new byte[byteList.size()];
        for(int i=0; i<byteList.size(); i++){
            stringContent[i] = byteList.get(i);
        }
        String content = new String(stringContent);
        return content;
    }


    public static int readInt(byte[] data, int startIndex){
        return bytes2int(copyOfRange(data, startIndex, 4));
    }

    public static int readShort(byte[] data, int startIndex){
        return bytes2short(copyOfRange(data, startIndex, 2));
    }

    public static String readUtf8String(byte[] data, int startIndex, int length) throws UnsupportedEncodingException {
        return new String(copyOfRange(data, startIndex, length), "UTF-8");
    }


}
