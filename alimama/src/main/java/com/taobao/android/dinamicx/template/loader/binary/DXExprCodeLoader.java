package com.taobao.android.dinamicx.template.loader.binary;

import android.util.Log;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXBranchBlockNode;
import com.taobao.android.dinamicx.expression.DXConstNode;
import com.taobao.android.dinamicx.expression.DXEventNode;
import com.taobao.android.dinamicx.expression.DXExprNode;
import com.taobao.android.dinamicx.expression.DXMethodNode;
import com.taobao.android.dinamicx.expression.DXSerialBlockNode;
import com.taobao.android.dinamicx.expression.DXVarNode;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import java.util.List;
import java.util.Stack;

public class DXExprCodeLoader {
    private static final String TAG = "CodeManager_TMTEST";
    private DXLongSparseArray<DXExprNode> mCodeMap;
    private Stack<DXExprNode> nodeStack = new Stack<>();
    private DXStringLoader varStringLoader;

    public DXExprCodeLoader(DXStringLoader dXStringLoader) {
        this.varStringLoader = dXStringLoader;
    }

    public DXExprNode get(long j) {
        if (this.mCodeMap != null) {
            return this.mCodeMap.get(j);
        }
        return null;
    }

    public boolean loadFromBuffer(int i, DXCodeReader dXCodeReader, DXRuntimeContext dXRuntimeContext) {
        DXExprNode dXExprNode;
        byte b = 1;
        if (i == 0) {
            return true;
        }
        dXCodeReader.getMaxSize();
        short readShort = dXCodeReader.readShort();
        if (readShort < 0) {
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_COUNT_ERROR, "count < 0"));
            return false;
        }
        this.mCodeMap = new DXLongSparseArray<>((int) readShort);
        int i2 = 0;
        boolean z = true;
        while (i2 < readShort) {
            long readLong = dXCodeReader.readLong();
            dXCodeReader.readShort();
            this.nodeStack.clear();
            byte readByte = dXCodeReader.readByte();
            DXExprNode dXExprNode2 = null;
            char c = 0;
            while (true) {
                switch (readByte) {
                    case 0:
                        if (dXExprNode2 != null) {
                            this.nodeStack.push(dXExprNode2);
                        }
                        byte readByte2 = dXCodeReader.readByte();
                        if (readByte2 == b) {
                            dXExprNode = new DXMethodNode();
                        } else if (readByte2 == 3) {
                            dXExprNode = new DXConstNode();
                        } else if (readByte2 == 5) {
                            dXExprNode = new DXBranchBlockNode();
                        } else if (readByte2 == 4) {
                            dXExprNode = new DXSerialBlockNode();
                        } else if (readByte2 == 2) {
                            dXExprNode = new DXVarNode();
                        } else if (readByte2 != 6) {
                            return false;
                        } else {
                            dXExprNode = new DXEventNode();
                        }
                        dXExprNode.type = readByte2;
                        dXExprNode.exprId = dXCodeReader.readLong();
                        dXExprNode.name = this.varStringLoader.getString(dXExprNode.exprId);
                        if (dXExprNode.name != null || readByte2 == 6 || readByte2 == b) {
                            dXExprNode2 = dXExprNode;
                            break;
                        } else {
                            List<DXError.DXErrorInfo> list = dXRuntimeContext.getDxError().dxErrorInfoList;
                            list.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_COMMON_ERROR, "exprNode.name == null && type != DXExprNode.Event && type != DXExprNode.Method exprid" + dXExprNode.exprId));
                            return false;
                        }
                    case 1:
                        if (this.nodeStack.size() <= 0) {
                            c = 1;
                            break;
                        } else {
                            DXExprNode pop = this.nodeStack.pop();
                            pop.addChildNode(dXExprNode2);
                            dXExprNode2 = pop;
                            break;
                        }
                    default:
                        Log.e(TAG, "load expr invalidate tag type:" + readByte);
                        List<DXError.DXErrorInfo> list2 = dXRuntimeContext.getDxError().dxErrorInfoList;
                        list2.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_BINARY_FILE_EXPR_LOADER_COMMON_ERROR, "load expr invalidate tag type:" + readByte));
                        z = false;
                        c = 2;
                        break;
                }
                if (c != 0) {
                    this.mCodeMap.put(readLong, dXExprNode2);
                    i2++;
                    b = 1;
                } else {
                    readByte = dXCodeReader.readByte();
                    b = 1;
                }
            }
        }
        if (!z) {
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", 70008));
        }
        return z;
    }
}
