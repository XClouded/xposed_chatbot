package com.taobao.android.dinamicx.template.loader.binary;

import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import java.util.HashMap;
import java.util.Map;

public class DXEnumLoader {
    private DXLongSparseArray<Map<String, Integer>> enumMap;
    private DXStringLoader varStringLoader;

    public DXEnumLoader(DXStringLoader dXStringLoader) {
        this.varStringLoader = dXStringLoader;
    }

    public boolean loadFromBuffer(int i, DXCodeReader dXCodeReader, DXRuntimeContext dXRuntimeContext) {
        int i2 = i;
        if (i2 == 0) {
            return true;
        }
        int pos = dXCodeReader.getPos();
        short readShort = dXCodeReader.readShort();
        if (readShort < 0) {
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_COUNT_ERROR, "totalSize < 0"));
            return false;
        }
        this.enumMap = new DXLongSparseArray<>((int) readShort);
        for (int i3 = 0; i3 < readShort; i3++) {
            long readLong = dXCodeReader.readLong();
            byte readByte = dXCodeReader.readByte();
            if (readByte <= 0) {
                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_ERROR, "count <= 0"));
                return false;
            }
            HashMap hashMap = new HashMap(readByte);
            for (int i4 = 0; i4 < readByte; i4++) {
                hashMap.put(this.varStringLoader.getString(dXCodeReader.readLong()), Integer.valueOf(dXCodeReader.readInt()));
            }
            this.enumMap.put(readLong, hashMap);
        }
        if (dXCodeReader.getPos() - pos == i2) {
            return true;
        }
        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_ENUM_LOADER_ERROR, "reader.getPos() - startPos != length"));
        return false;
    }

    public DXLongSparseArray<Map<String, Integer>> getEnumMap() {
        return this.enumMap;
    }
}
