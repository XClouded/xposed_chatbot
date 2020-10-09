package com.taobao.android.dinamicx;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

public class DXWidgetNodeParser {
    public void parseInMeasure(DXWidgetNode dXWidgetNode) {
        dXWidgetNode.setStatFlag(1024);
        if (parseWidgetNode(dXWidgetNode)) {
            int direction = dXWidgetNode.getDirection();
            for (int i = 0; i < dXWidgetNode.getChildrenCount(); i++) {
                DXWidgetNode childAt = dXWidgetNode.getChildAt(i);
                childAt.getDXRuntimeContext().setParentDirectionSpec(direction);
                parseInMeasure(childAt);
            }
        }
    }

    public DXWidgetNode parseWT(DXWidgetNode dXWidgetNode) {
        try {
            recursiveParseWT(dXWidgetNode);
            return dXWidgetNode;
        } catch (Exception e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
            dXWidgetNode.getDXRuntimeContext().getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXCEPTION));
            return null;
        }
    }

    private void recursiveParseWT(DXWidgetNode dXWidgetNode) {
        dXWidgetNode.unsetStatFlag(1024);
        if (parseWidgetNode(dXWidgetNode)) {
            int direction = dXWidgetNode.getDirection();
            for (int i = 0; i < dXWidgetNode.getChildrenCount(); i++) {
                DXWidgetNode childAt = dXWidgetNode.getChildAt(i);
                childAt.getDXRuntimeContext().setParentDirectionSpec(direction);
                recursiveParseWT(childAt);
            }
            dXWidgetNode.unsetStatFlag(1);
            dXWidgetNode.setStatFlag(32768);
            dXWidgetNode.setStatFlag(4);
        }
    }

    private boolean parseWidgetNode(DXWidgetNode dXWidgetNode) {
        if (isWidgetNodeGone(dXWidgetNode)) {
            return false;
        }
        if (dXWidgetNode.getStatInPrivateFlags(1024)) {
            if (dXWidgetNode.getLayoutWidth() == 0 || dXWidgetNode.getLayoutHeight() == 0) {
                parseAttrsInWidgetNode(dXWidgetNode);
                dXWidgetNode.setStatFlag(2);
            }
            if (!(dXWidgetNode.getLayoutWidth() == -2 || dXWidgetNode.getLayoutHeight() == -2)) {
                return false;
            }
        }
        if (!dXWidgetNode.getStatInPrivateFlags(2) || dXWidgetNode.getStatInPrivateFlags(1)) {
            parseAttrsInWidgetNode(dXWidgetNode);
            dXWidgetNode.setStatFlag(2);
        }
        if (!dXWidgetNode.getStatInPrivateFlags(4096)) {
            dXWidgetNode.setStatFlag(4096);
            if (dXWidgetNode.getChildrenCount() > 0) {
                dXWidgetNode.onBeforeBindChildData();
            }
        }
        return true;
    }

    public static boolean isWidgetNodeGone(DXWidgetNode dXWidgetNode) {
        Object obj;
        if (dXWidgetNode.getVisibility() == 2) {
            return true;
        }
        if (dXWidgetNode.getDataParsersExprNode() == null) {
            return false;
        }
        if (!dXWidgetNode.getStatInPrivateFlags(2048)) {
            if (dXWidgetNode.getDataParsersExprNode().get(DXHashConstant.DX_VIEW_VISIBILITY) != null) {
                try {
                    obj = dXWidgetNode.getDataParsersExprNode().get(DXHashConstant.DX_VIEW_VISIBILITY).evaluate((DXEvent) null, dXWidgetNode.getDXRuntimeContext());
                } catch (Exception e) {
                    if (DinamicXEngine.isDebug()) {
                        e.printStackTrace();
                    }
                    dXWidgetNode.getDXRuntimeContext().getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_EVALUEATE_EXCEPTION));
                    obj = null;
                }
                try {
                    if (obj instanceof String) {
                        String valueOf = String.valueOf(obj);
                        if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY) == null || dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY).get(valueOf) == null) {
                            dXWidgetNode.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, dXWidgetNode.getDefaultValueForIntAttr(DXHashConstant.DX_VIEW_VISIBILITY));
                        } else {
                            dXWidgetNode.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, ((Integer) dXWidgetNode.getEnumMap().get(DXHashConstant.DX_VIEW_VISIBILITY).get(valueOf)).intValue());
                        }
                    } else {
                        dXWidgetNode.setIntAttribute(DXHashConstant.DX_VIEW_VISIBILITY, dXWidgetNode.getDefaultValueForIntAttr(DXHashConstant.DX_VIEW_VISIBILITY));
                    }
                } catch (Exception e2) {
                    if (DinamicXEngine.isDebug()) {
                        e2.printStackTrace();
                    }
                    dXWidgetNode.getDXRuntimeContext().getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_SET_VALUE));
                }
            }
            dXWidgetNode.setStatFlag(2048);
        }
        if (dXWidgetNode.getVisibility() == 2) {
            return true;
        }
        return false;
    }

    private void parseAttrsInWidgetNode(DXWidgetNode dXWidgetNode) {
        JSONObject jSONObject;
        int i;
        if (dXWidgetNode.getDataParsersExprNode() != null) {
            DXRuntimeContext dXRuntimeContext = dXWidgetNode.getDXRuntimeContext();
            for (int i2 = 0; i2 < dXWidgetNode.getDataParsersExprNode().size(); i2++) {
                long keyAt = dXWidgetNode.getDataParsersExprNode().keyAt(i2);
                if (keyAt != DXHashConstant.DX_VIEW_VISIBILITY) {
                    DXExprNode valueAt = dXWidgetNode.getDataParsersExprNode().valueAt(i2);
                    try {
                        jSONObject = valueAt.evaluate((DXEvent) null, dXRuntimeContext);
                    } catch (Exception e) {
                        if (DinamicXEngine.isDebug()) {
                            e.printStackTrace();
                        }
                        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, "Pipeline_Stage_Load_Binary", DXError.DXERROR_PIPELINE_PARSE_WT_EXPR_EVALUEATE_EXCEPTION));
                        jSONObject = null;
                    }
                    short dataType = valueAt.getDataType();
                    if (dataType == 96) {
                        parseNumType(dXRuntimeContext.getContext(), dXWidgetNode, keyAt, jSONObject);
                    } else if (dataType == 608) {
                        String valueOf = String.valueOf(jSONObject);
                        if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(keyAt) == null || dXWidgetNode.getEnumMap().get(keyAt).get(valueOf) == null) {
                            parseNumType(dXRuntimeContext.getContext(), dXWidgetNode, keyAt, jSONObject);
                        } else {
                            dXWidgetNode.setIntAttribute(keyAt, ((Integer) dXWidgetNode.getEnumMap().get(keyAt).get(valueOf)).intValue());
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
                                            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_COLOR_EXCEPTION));
                                            Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Color failed, use default value.");
                                            i = dXWidgetNode.getDefaultValueForIntAttr(keyAt);
                                        }
                                    } else {
                                        i = dXWidgetNode.getDefaultValueForIntAttr(keyAt);
                                    }
                                    dXWidgetNode.setIntAttribute(keyAt, i);
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
                                                                dXWidgetNode.setIntAttribute(keyAt, Integer.parseInt(String.valueOf(jSONObject)));
                                                                break;
                                                            } catch (Exception unused2) {
                                                                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_INT_EXCEPTION));
                                                                Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Integer failed, use default value.");
                                                                setIntegerAttrDefault(dXWidgetNode, keyAt);
                                                                break;
                                                            }
                                                        } else {
                                                            setIntegerAttrDefault(dXWidgetNode, keyAt);
                                                            break;
                                                        }
                                                    case 2:
                                                        if (z) {
                                                            try {
                                                                dXWidgetNode.setLongAttribute(keyAt, Long.parseLong(String.valueOf(jSONObject)));
                                                                break;
                                                            } catch (Exception unused3) {
                                                                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_LONG_EXCEPTION));
                                                                Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Long failed, use default value.");
                                                                setLongAttrDefault(dXWidgetNode, keyAt);
                                                                break;
                                                            }
                                                        } else {
                                                            setLongAttrDefault(dXWidgetNode, keyAt);
                                                            break;
                                                        }
                                                }
                                            } else if (jSONObject != null) {
                                                dXWidgetNode.setObjAttribute(keyAt, jSONObject);
                                            } else {
                                                dXWidgetNode.setObjAttribute(keyAt, dXWidgetNode.getDefaultValueForObjectAttr(keyAt));
                                            }
                                        } else if (jSONObject instanceof String) {
                                            String valueOf2 = String.valueOf(jSONObject);
                                            if (dXWidgetNode.getEnumMap() == null || dXWidgetNode.getEnumMap().get(keyAt) == null || dXWidgetNode.getEnumMap().get(keyAt).get(valueOf2) == null) {
                                                dXWidgetNode.setIntAttribute(keyAt, dXWidgetNode.getDefaultValueForIntAttr(keyAt));
                                            } else {
                                                dXWidgetNode.setIntAttribute(keyAt, ((Integer) dXWidgetNode.getEnumMap().get(keyAt).get(valueOf2)).intValue());
                                            }
                                        } else {
                                            dXWidgetNode.setIntAttribute(keyAt, dXWidgetNode.getDefaultValueForIntAttr(keyAt));
                                        }
                                    } else if (!z || !(jSONObject instanceof JSONObject)) {
                                        setMapAttrDefault(dXWidgetNode, keyAt);
                                    } else {
                                        try {
                                            dXWidgetNode.setMapAttribute(keyAt, jSONObject);
                                        } catch (Exception unused4) {
                                            dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_MAP_EXCEPTION));
                                            Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse JsonObject&Map failed, use default value.");
                                            setMapAttrDefault(dXWidgetNode, keyAt);
                                        }
                                    }
                                } else if (!z || !(jSONObject instanceof JSONArray)) {
                                    setListAttrDefault(dXWidgetNode, keyAt);
                                } else {
                                    try {
                                        dXWidgetNode.setListAttribute(keyAt, (JSONArray) jSONObject);
                                    } catch (Exception unused5) {
                                        dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_LIST_EXCEPTION));
                                        Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse JsonArray&List failed, use default value.");
                                        setListAttrDefault(dXWidgetNode, keyAt);
                                    }
                                }
                            } else if (!z || !(jSONObject instanceof String)) {
                                dXWidgetNode.setStringAttribute(keyAt, dXWidgetNode.getDefaultValueForStringAttr(keyAt));
                            } else {
                                dXWidgetNode.setStringAttribute(keyAt, (String) jSONObject);
                            }
                        } else if (!z) {
                            setDoubleAttrDefault(dXWidgetNode, keyAt);
                        } else {
                            try {
                                dXWidgetNode.setDoubleAttribute(keyAt, Double.parseDouble(String.valueOf(jSONObject)));
                            } catch (Exception unused6) {
                                dXRuntimeContext.getDxError().dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_PARSE_WT, DXError.DXERROR_PIPELINE_PARSE_WT_DOUBLE_EXCEPTION));
                                Log.i("DXTemplateParser", Operators.ARRAY_START_STR + keyAt + SymbolExpUtil.SYMBOL_EQUAL + jSONObject + "] parse Double failed, use default value.");
                                setDoubleAttrDefault(dXWidgetNode, keyAt);
                            }
                        }
                    }
                }
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
