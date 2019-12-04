package dexclass;

import common.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DexClassData {
    private DexClassDataHeader header;
    private List<DexField> staticFields = new ArrayList<>();
    private List<DexField> instanceFields = new ArrayList<>();
    private List<DexMethod> directMethods = new ArrayList<>();
    private List<DexMethod> virtualMethods = new ArrayList<>();

    public void init(byte[] fileData, int startIndex) {
        header = new DexClassDataHeader();

        int nextIndex;
        int staticFieldsSize, instanceFieldsSize, directMethodsSize, virtualMethodsSize;
        Map<String, Integer> resultMap = Util.readMapULeb128(fileData, startIndex);
        staticFieldsSize = resultMap.get("result");
        nextIndex = resultMap.get("nextIndex");

        resultMap = Util.readMapULeb128(fileData, nextIndex);
        instanceFieldsSize = resultMap.get("result");
        nextIndex = resultMap.get("nextIndex");

        resultMap = Util.readMapULeb128(fileData, nextIndex);
        directMethodsSize = resultMap.get("result");
        nextIndex = resultMap.get("nextIndex");

        resultMap = Util.readMapULeb128(fileData, nextIndex);
        virtualMethodsSize = resultMap.get("result");
        nextIndex = resultMap.get("nextIndex");

        header.setStaticFieldsSize(staticFieldsSize);
        header.setInstanceFieldsSize(instanceFieldsSize);
        header.setDirectMethodsSize(directMethodsSize);
        header.setVirtualMethodsSize(virtualMethodsSize);

        for(int i=0; i<header.getStaticFieldsSize(); i++){
            DexField dexField = new DexField();
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int fieldIdx = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int accessFlags  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            dexField.setFieldIdx(fieldIdx);
            dexField.setAccessFlags(accessFlags);
            staticFields.add(dexField);
        }


        for(int i=0; i<header.getInstanceFieldsSize(); i++){
            DexField dexField = new DexField();
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int fieldIdx = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int accessFlags  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            dexField.setFieldIdx(fieldIdx);
            dexField.setAccessFlags(accessFlags);
            instanceFields.add(dexField);
        }

        for(int i=0; i<header.getDirectMethodsSize(); i++){
            DexMethod dexMethod = new DexMethod();
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int methodIdx = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int accessFlags  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int codeOff  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");

            dexMethod.setMethodIdx(methodIdx);
            dexMethod.setAccessFlags(accessFlags);
            dexMethod.setCodeOff(codeOff);
            dexMethod.init(fileData);
            directMethods.add(dexMethod);
        }


        for(int i=0; i<header.getVirtualMethodsSize(); i++){
            DexMethod dexMethod = new DexMethod();
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int methodIdx = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int accessFlags  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");
            resultMap = Util.readMapULeb128(fileData, nextIndex);
            int codeOff  = resultMap.get("result");
            nextIndex = resultMap.get("nextIndex");

            dexMethod.setMethodIdx(methodIdx);
            dexMethod.setAccessFlags(accessFlags);
            dexMethod.setCodeOff(codeOff);
            dexMethod.init(fileData);
            virtualMethods.add(dexMethod);
        }

    }
}
