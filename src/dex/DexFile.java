package dex;

import common.Util;
import dexclass.DexClassData;
import dexclass.DexField;
import dexclass.DexMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DexFile {
    private byte originFileData[];
    private DexHeader dexHeader = new DexHeader();
    private List<String> dexStringIdArrayList = new ArrayList<>();
    private List<DexTypeId> dexTypeIdArrayList = new ArrayList<>();
    private List<DexProtoId> dexProtoIdArrayList = new ArrayList<>();
    private List<DexFieldId> dexFieldIdArrayList = new ArrayList<>();
    private List<DexMethodId> dexMethodIdArrayList = new ArrayList<>();
    private List<DexClassDef> dexClassDefArrayList = new ArrayList<>();

    public DexFile(String dexFilePath) throws Exception{
        File dexFile = new File(dexFilePath);
        if(!dexFile.exists()){
            throw new Exception(dexFilePath + "not exist");
        }
        originFileData = new byte[(int)dexFile.length()];
        new FileInputStream(dexFile).read(originFileData);
        this.parse();
    }


    private void parse() throws UnsupportedEncodingException {
        System.out.println("start processing dexFileHeader");
        // 头
        this.initDexHeader();
        // 索引区
        this.initDexStringIdArrayList();
        this.initDexTypeIdArrayList();
        this.initDexProtoIdArrayList();
        this.initDexFieldIdArrayList();
        this.initDexMethodIdArrayList();
//        this.initDexMapListList();
        // 数据区
        this.initDexClassDefArrayList();

        System.out.println("parse end");
    }

    private void initDexHeader(){
        this.dexHeader.init(this.originFileData);
    }

    private void initDexStringIdArrayList() throws UnsupportedEncodingException {
        for(int i=0; i< this.dexHeader.getStringIdsSize(); i++){
            int offset = dexHeader.getStringIdsOff() + 4*i;

            int strOffset = Util.bytes2int(Util.copyOfRange(originFileData, offset, 4));

            Map<String, Integer> resultMap = Util.readMapULeb128(originFileData, strOffset);
            int strLength = resultMap.get("result");
            int nextIndex = resultMap.get("nextIndex");
            String str = new String(Arrays.copyOfRange(originFileData, nextIndex, nextIndex+strLength), "UTF-8");
            dexStringIdArrayList.add(str);
        }
    }


    private void initDexTypeIdArrayList(){
        for(int i=0; i<dexHeader.getTypeIdsSIze(); i++){
            DexTypeId dexTypeId = new DexTypeId();
            int offset = dexHeader.getTypeIdsOff() + i*4;
            int descriptorIdx = Util.bytes2int(Util.copyOfRange(originFileData, offset, 4));
            dexTypeId.setDescriptorIdx(descriptorIdx);
            dexTypeIdArrayList.add(dexTypeId);
        }
    }


    private void initDexProtoIdArrayList(){
        for(int i=0; i<dexHeader.getProtoIdsSize(); i++){
            DexProtoId dexProtoId = new DexProtoId();
            int offset = dexHeader.getProtoIdsOff() + i*12; // 一个DexProtoId 12个字节
            dexProtoId.init(originFileData, offset);
            dexProtoIdArrayList.add(dexProtoId);
        }
    }

    private void initDexFieldIdArrayList(){
        for(int i=0; i<dexHeader.getFieldIdsSize(); i++){
            DexFieldId dexFieldId = new DexFieldId();
            int offset = dexHeader.getFieldIdsOff() + i*8; // 一个DexProtoId 12个字节
            dexFieldId.init(originFileData, offset);
            dexFieldIdArrayList.add(dexFieldId);
        }
    }

    private void initDexMethodIdArrayList(){
        for(int i=0; i<dexHeader.getMethodIdsSize(); i++){
            DexMethodId dexMethodId = new DexMethodId();
            int offset = dexHeader.getMethodIdsOff() + i*8; // 一个DexProtoId 12个字节
            dexMethodId.init(originFileData, offset);
            dexMethodIdArrayList.add(dexMethodId);
        }
    }


    private void initDexClassDefArrayList(){
        for(int i=0; i<dexHeader.getClassDefsSize(); i++){
            DexClassDef dexClassDef = new DexClassDef();
            int offset = dexHeader.getClassDefsOff() + i*8*4;
            dexClassDef.init(originFileData, offset);
            dexClassDefArrayList.add(dexClassDef);
        }
    }





    public void show(){
        this.showHeader();
        this.showDexStringIds();
        this.showDexTypeIds();
        this.showDexProtoIds();
        this.showDexFieldIds();
        this.showDexMethodIds();
        this.showClassDefs();
    }


    private void showHeader(){
        dexHeader.show();
    }

    private String getDexStringByIndex(int index){
        return dexStringIdArrayList.get(index);
    }

    private String getDexTypeByIndex(int index){
        return dexStringIdArrayList.get(dexTypeIdArrayList.get(index).getDescriptorIdx());
    }

    private void showDexStringIds(){
        System.out.println("DexStringIds :");
        for(int i=0; i<dexStringIdArrayList.size(); i++){
            System.out.printf("  [%3d] %s\n", i, getDexStringByIndex(i));
        }
    }
    private void showDexTypeIds(){
        System.out.println("DexTypeIds :");
        for(int i=0; i<dexTypeIdArrayList.size(); i++){
            System.out.printf("  [%3d] %s\n", i, getDexTypeByIndex(i));
        }
    }

    private String getDexProtoId(int index){
        StringBuilder sb = new StringBuilder();
        DexProtoId dexProtoId = dexProtoIdArrayList.get(index);
        sb.append(getDexStringByIndex(dexProtoId.getShortyIdx()) + "  ");  // 简写
        sb.append(getDexTypeByIndex(dexProtoId.getReturnTypeIdx()));  // 返回值类型
        sb.append("(");
        DexTypeList dexTypeList = dexProtoId.parametersDexTypeList(originFileData);
        if(dexTypeList != null){
            for(int j=0; j< dexTypeList.getSize(); j++){
                String paramType =getDexTypeByIndex(dexTypeList.getList().get(j).getTypeIdx());
                sb.append(paramType);
                if(j+1 != dexTypeList.getSize()){
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private void showDexProtoIds(){
        System.out.println("DexProtoIds :");
        for(int i=0; i<dexProtoIdArrayList.size(); i++){
            System.out.printf("  [%3d] %s\n", i, getDexProtoId(i));
        }
    }

    private String getDexFieldId(int index){
        StringBuilder sb = new StringBuilder();
        DexFieldId dexFieldId = dexFieldIdArrayList.get(index);
        sb.append("class: ");
        sb.append(getDexTypeByIndex(dexFieldId.getClassIdx())); // 类型
        sb.append("  type: ");
        sb.append(getDexTypeByIndex(dexFieldId.getTypeIdx()));
        sb.append("  name: ");
        sb.append(getDexStringByIndex(dexFieldId.getNameIdx()));
        return sb.toString();
    }

    private void showDexFieldIds(){
        System.out.println("DexFieldIds :");
        for(int i=0; i<dexFieldIdArrayList.size(); i++){
            System.out.printf("  [%3d] %s\n", i, getDexFieldId(i));
        }
    }

    private String getDexMethodId(int index){
        StringBuilder sb = new StringBuilder();
        DexMethodId dexMethodId = dexMethodIdArrayList.get(index);
        sb.append("class: ");
        sb.append(getDexTypeByIndex(dexMethodId.getClassIdx()));
        sb. append("  name: ");
        sb.append(getDexStringByIndex(dexMethodId.getNameIdx()));
        sb.append("  proto:  ");
        sb.append(getDexProtoId(dexMethodId.getProtoIdx()));
        return sb.toString();
    }
    private void showDexMethodIds(){
        System.out.println("DexMethodIds :");
        for(int i=0; i<dexMethodIdArrayList.size(); i++){
            System.out.printf("  [%3d] %s\n", i, getDexMethodId(i));
        }
    }

    private String getClassDef(int index){
        DexClassDef classDef = dexClassDefArrayList.get(index);
        StringBuilder sb = new StringBuilder();
        sb.append("classIdx: ");
        sb.append(getDexTypeByIndex(classDef.getClassIdx()));
        sb.append("\n   ");

        sb.append("accessFlags: ");
        sb.append(String.format("0x%X", classDef.getAccessFlags()));
        sb.append("\n   ");

        sb.append("superclassIdx: ");
        sb.append(getDexTypeByIndex(classDef.getSuperclassIdx()));
        sb.append("\n   ");

        sb.append("interfacesOff: ");
        sb.append(String.format("0x%X", classDef.getInterfacesOff()));
        sb.append("\n   ");

        sb.append("sourceFileIdx: ");
        if(classDef.getSourceFileIdx() > 0){ // 没有源文件 0xffffffff -1
            sb.append(getDexStringByIndex(classDef.getSourceFileIdx()));
        }
        sb.append("\n   ");

        sb.append("annotationsOff: ");
        sb.append(String.format("0x%X", classDef.getAnnotationsOff()));
        sb.append("\n   ");

        sb.append("staticValuesOff: ");
        sb.append(String.format("0x%X", classDef.getStaticValuesOff()));
        sb.append("\n   ");

        sb.append("DexClassData: ");
        sb.append("\n       ");

        if(classDef.getClassDataOff() !=0 ){
            DexClassData dexClassData = new DexClassData();
            dexClassData.init(originFileData, classDef.getClassDataOff());

            sb.append("staticFieldsSize : "+dexClassData.getHeader().getStaticFieldsSize());
            sb.append("\n       ");
            sb.append("instanceFieldsSize : "+dexClassData.getHeader().getInstanceFieldsSize());
            sb.append("\n       ");
            sb.append("directMethodsSize : "+dexClassData.getHeader().getDirectMethodsSize());
            sb.append("\n       ");
            sb.append("virtualMethodsSize : "+dexClassData.getHeader().getVirtualMethodsSize());
            sb.append("\n       staticFields :");
            for(int i=0; i<dexClassData.getStaticFields().size(); i++){
                DexField dexField = dexClassData.getStaticFields().get(i);
                sb.append(String.format("\n           [%2d] %s  %s",i, dexField.show(), getDexFieldId(dexField.getFieldIdx())));
            }
            sb.append("\n       instanceFields :");
            for(int i=0; i<dexClassData.getInstanceFields().size(); i++){
                DexField dexField = dexClassData.getInstanceFields().get(i);
                sb.append(String.format("\n           [%2d] %s  %s",i, dexField.show(), getDexFieldId(dexField.getFieldIdx())));
            }

            sb.append("\n       directMethods :");
            for(int i=0; i<dexClassData.getDirectMethods().size(); i++){
                DexMethod dexMethod = dexClassData.getDirectMethods().get(i);
                sb.append(String.format("\n           [%2d] methodIdx: %d  accessFlags: 0x%X codeOff: 0x%X ",
                        i, dexMethod.getMethodIdx(), dexMethod.getAccessFlags(), dexMethod.getCodeOff()));
                sb.append("\n                ");
                if(dexMethod.getDexCode() != null){
                    sb.append(dexMethod.getDexCode().show(originFileData));
                }

            }
            sb.append("\n       virtualMethods :");
            for(int i=0; i<dexClassData.getVirtualMethods().size(); i++){
                DexMethod dexMethod = dexClassData.getVirtualMethods().get(i);
                sb.append(String.format("\n           [%2d] methodIdx: %d  accessFlags: 0x%X codeOff: 0x%X ",
                        i, dexMethod.getMethodIdx(), dexMethod.getAccessFlags(), dexMethod.getCodeOff()));
                sb.append("\n                ");
                if(dexMethod.getDexCode() != null){
                    sb.append(dexMethod.getDexCode().show(originFileData));
                }
            }
        }
        return sb.toString();
    }


    private void showClassDefs(){
        System.out.println("ClassDefs :");
        for(int i=0; i<dexClassDefArrayList.size(); i++){
            System.out.printf("  [%3d]\n   %s\n", i, getClassDef(i));
        }
    }



}
