package com.taobao.android.dinamicx.template.loader.binary;

import android.util.Log;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;

public class DXStringLoader implements IDXStringSupport {
    private static final String TAG = "StringLoader_TMTEST";
    private DXLongSparseArray<String> mIndex2String;

    public boolean loadFromBuffer(int i, DXCodeReader dXCodeReader, DXRuntimeContext dXRuntimeContext) {
        if (i == 0) {
            return true;
        }
        int maxSize = dXCodeReader.getMaxSize();
        short readShort = dXCodeReader.readShort();
        if (readShort < 0) {
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_COUNT_ERROR));
            return false;
        }
        this.mIndex2String = new DXLongSparseArray<>((int) readShort);
        int i2 = 0;
        while (i2 < readShort) {
            long readLong = dXCodeReader.readLong();
            short readShort2 = dXCodeReader.readShort();
            if (dXCodeReader.getPos() + readShort2 <= maxSize) {
                this.mIndex2String.put(readLong, new String(dXCodeReader.getCode(), dXCodeReader.getPos(), readShort2));
                dXCodeReader.seekBy(readShort2);
                i2++;
            } else {
                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_VARSTRING_LOADER_ERROR));
                Log.e(TAG, "read string over");
                return false;
            }
        }
        return true;
    }

    public String getString(long j) {
        if (this.mIndex2String != null && this.mIndex2String.get(j) != null) {
            return this.mIndex2String.get(j);
        }
        if (!DinamicXEngine.isDebug()) {
            return null;
        }
        Log.e(TAG, "getString null:" + j);
        return null;
    }
}
