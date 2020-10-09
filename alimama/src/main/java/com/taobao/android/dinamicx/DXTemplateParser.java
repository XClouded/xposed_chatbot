package com.taobao.android.dinamicx;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import androidx.collection.LongSparseArray;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.expression.DXExprNode;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.weex.el.parse.Operators;
import mtopsdk.common.util.SymbolExpUtil;

public class DXTemplateParser {
    @Deprecated
    public DXWidgetNode parseWT(DXWidgetNode dXWidgetNode, DXRuntimeContext dXRuntimeContext) {
        try {
            recursiveParseWT(dXWidgetNode, dXRuntimeContext);
            return dXWidgetNode;
        } catch (Exception e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXCEPTION));
            return null;
        }
    }

    private void recursiveParseWT(DXWidgetNode dXWidgetNode, DXRuntimeContext dXRuntimeContext) {
        JSONObject jSONObject;
        int i;
        Object obj;
        DXWidgetNode dXWidgetNode2 = dXWidgetNode;
        DXRuntimeContext dXRuntimeContext2 = dXRuntimeContext;
        if (dXWidgetNode.getVisibility() != 2) {
            if (dXWidgetNode.getDataParsersExprNode() != null) {
                LongSparseArray<DXExprNode> dataParsersExprNode = dXWidgetNode.getDataParsersExprNode();
                long j = DXHashConstant.DX_VIEW_VISIBILITY;
                if (dataParsersExprNode.get(DXHashConstant.DX_VIEW_VISIBILITY) != null) {
                    try {
                        obj = dXWidgetNode.getDataParsersExprNode().get(DXHashConstant.DX_VIEW_VISIBILITY).evaluate((DXEvent) null, dXRuntimeContext2);
                    } catch (Exception e) {
                        Exception exc = e;
                        if (DinamicXEngine.isDebug()) {
                            exc.printStackTrace();
                        }
                        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_EVALUEATE_EXCEPTION));
                        obj = null;
                    }
                    try {
                        if (obj instanceof String) {
                            String valueOf = String.valueOf(obj);
                            if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY) == null || dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY).get(valueOf) == null) {
                                dXWidgetNode2.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, dXWidgetNode2.getDefaultValueForIntAttr(DXHashConstant.DX_VIEW_VISIBILITY));
                            } else {
                                dXWidgetNode2.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, ((Integer) dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY).get(valueOf)).intValue());
                                if (dXWidgetNode.getVisibility() == 2) {
                                    return;
                                }
                            }
                        } else {
                            dXWidgetNode2.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, dXWidgetNode2.getDefaultValueForIntAttr(DXHashConstant.DX_VIEW_VISIBILITY));
                        }
                    } catch (Exception e2) {
                        if (DinamicXEngine.isDebug()) {
                            e2.printStackTrace();
                        }
                        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_SET_VALUE));
                    }
                }
                int i2 = 0;
                while (i2 < dXWidgetNode.getDataParsersExprNode().size()) {
                    long keyAt = dXWidgetNode.getDataParsersExprNode().keyAt(i2);
                    if (keyAt != j) {
                        DXExprNode valueAt = dXWidgetNode.getDataParsersExprNode().valueAt(i2);
                        try {
                            jSONObject = valueAt.evaluate((DXEvent) null, dXRuntimeContext2);
                        } catch (Exception e3) {
                            Exception exc2 = e3;
                            if (DinamicXEngine.isDebug()) {
                                exc2.printStackTrace();
                            }
                            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_EVALUEATE_EXCEPTION));
                            jSONObject = null;
                        }
                        short dataType = valueAt.getDataType();
                        if (dataType == 96) {
                            parseNumType(dXRuntimeContext.getContext(), dXWidgetNode, keyAt, jSONObject);
                        } else if (dataType == 608) {
                            String valueOf2 = String.valueOf(jSONObject);
                            if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(keyAt) == null || dXWidgetNode.getEnumMap().get(keyAt).get(valueOf2) == null) {
                                parseNumType(dXRuntimeContext.getContext(), dXWidgetNode, keyAt, jSONObject);
                            } else {
                                dXWidgetNode2.setIntAttribute(keyAt, ((Integer) dXWidgetNode.getEnumMap().get(keyAt).get(valueOf2)).intValue());
                            }
                        } else {
                            boolean z = jSONObject != null;
                            if (dataType != 4) {
                                if (dataType != 8) {
                                    if (dataType == 16) {
                                        if (z) {
                                            try {
                                                i = Color.parseColor(String.valueOf(jSONObject));
                                            } catch (Exception unused) {
                                                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_COLOR_EXCEPTION, " color obj " + jSONObject));
                                                Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Color failed, use default value.");
                                                i = dXWidgetNode2.getDefaultValueForIntAttr(keyAt);
                                            }
                                        } else {
                                            i = dXWidgetNode2.getDefaultValueForIntAttr(keyAt);
                                        }
                                        dXWidgetNode2.setIntAttribute(keyAt, i);
                                    } else if (dataType == 32 || dataType == 64) {
                                        parseNumType(dXRuntimeContext.getContext(), dXWidgetNode, keyAt, jSONObject);
                                    } else if (dataType != 128) {
                                        if (dataType != 256) {
                                            if (dataType != 512) {
                                                if (dataType != 1024) {
                                                    switch (dataType) {
                                                        case 1:
                                                            if (z) {
                                                                try {
                                                                    dXWidgetNode2.setIntAttribute(keyAt, Integer.parseInt(String.valueOf(jSONObject)));
                                                                    break;
                                                                } catch (Exception unused2) {
                                                                    dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_INT_EXCEPTION));
                                                                    Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Integer failed, use default value.");
                                                                    setIntegerAttrDefault(dXWidgetNode2, keyAt);
                                                                    break;
                                                                }
                                                            } else {
                                                                setIntegerAttrDefault(dXWidgetNode2, keyAt);
                                                                break;
                                                            }
                                                        case 2:
                                                            if (z) {
                                                                try {
                                                                    dXWidgetNode2.setLongAttribute(keyAt, Long.parseLong(String.valueOf(jSONObject)));
                                                                    break;
                                                                } catch (Exception unused3) {
                                                                    dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_LONG_EXCEPTION));
                                                                    Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Long failed, use default value.");
                                                                    setLongAttrDefault(dXWidgetNode2, keyAt);
                                                                    break;
                                                                }
                                                            } else {
                                                                setLongAttrDefault(dXWidgetNode2, keyAt);
                                                                break;
                                                            }
                                                    }
                                                } else if (jSONObject != null) {
                                                    dXWidgetNode2.setObjAttribute(keyAt, jSONObject);
                                                } else {
                                                    dXWidgetNode2.setObjAttribute(keyAt, dXWidgetNode2.getDefaultValueForObjectAttr(keyAt));
                                                }
                                            } else if (jSONObject instanceof String) {
                                                String valueOf3 = String.valueOf(jSONObject);
                                                if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(keyAt) == null || dXWidgetNode.getEnumMap().get(keyAt).get(valueOf3) == null) {
                                                    dXWidgetNode2.setIntAttribute(keyAt, dXWidgetNode2.getDefaultValueForIntAttr(keyAt));
                                                } else {
                                                    dXWidgetNode2.setIntAttribute(keyAt, ((Integer) dXWidgetNode.getEnumMap().get(keyAt).get(valueOf3)).intValue());
                                                }
                                            } else {
                                                dXWidgetNode2.setIntAttribute(keyAt, dXWidgetNode2.getDefaultValueForIntAttr(keyAt));
                                            }
                                        } else if (!z || !(jSONObject instanceof JSONObject)) {
                                            setMapAttrDefault(dXWidgetNode2, keyAt);
                                        } else {
                                            try {
                                                dXWidgetNode2.setMapAttribute(keyAt, jSONObject);
                                            } catch (Exception unused4) {
                                                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_MAP_EXCEPTION));
                                                Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse JsonObject&Map failed, use default value.");
                                                setMapAttrDefault(dXWidgetNode2, keyAt);
                                            }
                                        }
                                    } else if (!z || !(jSONObject instanceof JSONArray)) {
                                        setListAttrDefault(dXWidgetNode2, keyAt);
                                    } else {
                                        try {
                                            dXWidgetNode2.setListAttribute(keyAt, (JSONArray) jSONObject);
                                        } catch (Exception unused5) {
                                            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_LIST_EXCEPTION));
                                            Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse JsonArray&List failed, use default value.");
                                            setListAttrDefault(dXWidgetNode2, keyAt);
                                        }
                                    }
                                } else if (!z || !(jSONObject instanceof String)) {
                                    dXWidgetNode2.setStringAttribute(keyAt, dXWidgetNode2.getDefaultValueForStringAttr(keyAt));
                                } else {
                                    dXWidgetNode2.setStringAttribute(keyAt, (String) jSONObject);
                                }
                            } else if (!z) {
                                setDoubleAttrDefault(dXWidgetNode2, keyAt);
                            } else {
                                try {
                                    dXWidgetNode2.setDoubleAttribute(keyAt, Double.parseDouble(String.valueOf(jSONObject)));
                                } catch (Exception unused6) {
                                    dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_DOUBLE_EXCEPTION));
                                    Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Double failed, use default value.");
                                    setDoubleAttrDefault(dXWidgetNode2, keyAt);
                                }
                            }
                        }
                    }
                    i2++;
                    j = DXHashConstant.DX_VIEW_VISIBILITY;
                }
            }
            dXWidgetNode.onBeforeBindChildData();
            int direction = dXWidgetNode.getDirection();
            for (int i3 = 0; i3 < dXWidgetNode.getChildrenCount(); i3++) {
                DXWidgetNode childAt = dXWidgetNode2.getChildAt(i3);
                childAt.getDXRuntimeContext().setParentDirectionSpec(direction);
                recursiveParseWT(childAt, childAt.getDXRuntimeContext());
            }
        }
    }

    private void setIntegerAttrDefault(DXWidgetNode dXWidgetNode, long j) {
        dXWidgetNode.setIntAttribute(j, dXWidgetNode.getDefaultValueForIntAttr(j));
    }

    private void setLongAttrDefault(DXWidgetNode dXWidgetNode, long j) {
        dXWidgetNode.setLongAttribute(j, dXWidgetNode.getDefaultValueForLongAttr(j));
    }

    private void setDoubleAttrDefault(DXWidgetNode dXWidgetNode, long j) {
        dXWidgetNode.setDoubleAttribute(j, dXWidgetNode.getDefaultValueForDoubleAttr(j));
    }

    private void setListAttrDefault(DXWidgetNode dXWidgetNode, long j) {
        dXWidgetNode.setListAttribute(j, dXWidgetNode.getDefaultValueForListAttr(j));
    }

    private void setMapAttrDefault(DXWidgetNode dXWidgetNode, long j) {
        dXWidgetNode.setMapAttribute(j, dXWidgetNode.getDefaultValueForMapAttr(j));
    }

    private void parseNumType(Context context, DXWidgetNode dXWidgetNode, long j, Object obj) {
        dXWidgetNode.setIntAttribute(j, DXScreenTool.getPx(context, String.valueOf(obj), dXWidgetNode.getDefaultValueForIntAttr(j)));
    }
}
