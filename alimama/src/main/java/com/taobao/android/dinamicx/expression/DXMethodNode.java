package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.expression.parser.DXAbsFastReturnDinamicDataParser;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import java.util.List;

public class DXMethodNode extends DXExprNode {
    private List middle;

    public static class DXBoolean {
        public boolean value;
    }

    public byte getType() {
        return 1;
    }

    public List getMiddle() {
        return this.middle;
    }

    public void setMiddle(List list) {
        this.middle = list;
    }

    public DXMethodNode() {
        this.type = 1;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        boolean z;
        DXAbsFastReturnDinamicDataParser dXAbsFastReturnDinamicDataParser;
        Object obj = null;
        try {
            IDXDataParser iDXDataParser = this.exprId != 0 ? dXRuntimeContext.getParserMap().get(this.exprId) : null;
            if (iDXDataParser == null) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_AST_NODE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_AST_METHOD_NODE, DXError.DX_ERROR_CODE_PARSE_NOT_FOUND);
                dXErrorInfo.reason = "exprId:" + this.exprId;
                dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo);
                return null;
            }
            int i = 0;
            if (iDXDataParser instanceof DXAbsFastReturnDinamicDataParser) {
                dXAbsFastReturnDinamicDataParser = (DXAbsFastReturnDinamicDataParser) iDXDataParser;
                z = true;
            } else {
                dXAbsFastReturnDinamicDataParser = null;
                z = false;
            }
            int size = this.children != null ? this.children.size() : 0;
            DXBoolean dXBoolean = new DXBoolean();
            Object[] objArr = new Object[size];
            while (true) {
                if (i >= size) {
                    break;
                }
                objArr[i] = ((DXExprNode) this.children.get(i)).evaluate(dXEvent, dXRuntimeContext);
                if (z) {
                    Object evalWithArgs = dXAbsFastReturnDinamicDataParser.evalWithArgs(objArr, dXRuntimeContext, dXBoolean, i);
                    try {
                        if (dXBoolean.value) {
                            obj = evalWithArgs;
                            break;
                        }
                        obj = evalWithArgs;
                    } catch (Throwable th) {
                        th = th;
                        obj = evalWithArgs;
                        DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_AST_NODE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_AST_METHOD_NODE, DXError.DX_ERROR_CODE_METHOD_NODE_EXECUTE_EXCEPTION);
                        dXErrorInfo2.reason = DXExceptionUtil.getStackTrace(th);
                        dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo2);
                        return obj;
                    }
                }
                i++;
            }
            if (!z) {
                return iDXDataParser.evalWithArgs(objArr, dXRuntimeContext);
            }
            return obj;
        } catch (Throwable th2) {
            th = th2;
            DXError.DXErrorInfo dXErrorInfo22 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_AST_NODE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_AST_METHOD_NODE, DXError.DX_ERROR_CODE_METHOD_NODE_EXECUTE_EXCEPTION);
            dXErrorInfo22.reason = DXExceptionUtil.getStackTrace(th);
            dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo22);
            return obj;
        }
    }

    public IDXDataParser getDataParser(DXRuntimeContext dXRuntimeContext, long j) {
        if (dXRuntimeContext == null || dXRuntimeContext.getParserMap() == null) {
            return null;
        }
        return dXRuntimeContext.getParserMap().get(j);
    }
}
