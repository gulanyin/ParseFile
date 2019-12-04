package dexclass;

import lombok.Data;

@Data
public class DexClassDataHeader {
    private int staticFieldsSize;
    private int instanceFieldsSize;
    private int directMethodsSize;
    private int virtualMethodsSize;

}
