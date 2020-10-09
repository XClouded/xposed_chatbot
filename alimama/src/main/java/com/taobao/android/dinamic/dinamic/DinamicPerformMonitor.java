package com.taobao.android.dinamic.dinamic;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import mtopsdk.common.util.SymbolExpUtil;

public class DinamicPerformMonitor {
    private static final String PAGE = "Dinamic";
    private static final String TAG = "Dinamic_2";
    DinamicAppMonitor dinamicAppMonitor;

    public DinamicPerformMonitor(DinamicAppMonitor dinamicAppMonitor2) {
        this.dinamicAppMonitor = dinamicAppMonitor2;
    }

    public void trackCreateView(String str, DinamicTemplate dinamicTemplate, boolean z, DinamicError dinamicError, double d) {
        String allErrorDescription;
        String str2;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate);
            appendStr(createArg, "module", str);
            if (z) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "CreateView", createArg.toString());
                this.dinamicAppMonitor.counter_commit("Dinamic", "CreateView", createArg.toString(), d);
                DinamicLog.remoteLogi(TAG, TAG, buildRemoteLogContent(str, "trackCreateView", dinamicTemplate, (String) null));
                return;
            }
            DinamicAppMonitor dinamicAppMonitor2 = this.dinamicAppMonitor;
            String sb = createArg.toString();
            String obj = (dinamicError == null || dinamicError.isEmpty()) ? "" : dinamicError.getErrorMap().keySet().toString();
            if (dinamicError == null) {
                allErrorDescription = "";
            } else {
                allErrorDescription = dinamicError.getAllErrorDescription();
            }
            dinamicAppMonitor2.alarm_commitFail("Dinamic", "CreateView", sb, obj, allErrorDescription);
            if (dinamicError == null) {
                str2 = "";
            } else {
                str2 = dinamicError.getAllErrorDescription();
            }
            DinamicLog.remoteLoge(TAG, TAG, buildRemoteLogContent(str, "trackCreateView", dinamicTemplate, str2));
        }
    }

    private String buildRemoteLogContent(String str, String str2, DinamicTemplate dinamicTemplate, String str3) {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.ARRAY_START_STR);
        sb.append(str);
        sb.append("]:");
        sb.append(str2);
        sb.append("|");
        if (dinamicTemplate == null) {
            return sb.toString();
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("template", (Object) dinamicTemplate.name);
        jSONObject.put("version", (Object) dinamicTemplate.version);
        if (str3 != null) {
            jSONObject.put("error", (Object) str3);
        }
        sb.append(jSONObject.toJSONString());
        return sb.toString();
    }

    public void trackBindData(String str, DinamicTemplate dinamicTemplate, boolean z, DinamicError dinamicError, double d) {
        String allErrorDescription;
        String str2;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate);
            appendStr(createArg, "module", str);
            if (z) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "BindData", createArg.toString());
                this.dinamicAppMonitor.counter_commit("Dinamic", "BindData", createArg.toString(), d);
                DinamicLog.remoteLogi(TAG, TAG, buildRemoteLogContent(str, "trackBindData", dinamicTemplate, (String) null));
                return;
            }
            DinamicAppMonitor dinamicAppMonitor2 = this.dinamicAppMonitor;
            String sb = createArg.toString();
            String obj = (dinamicError == null || dinamicError.isEmpty()) ? "" : dinamicError.getErrorMap().keySet().toString();
            if (dinamicError == null) {
                allErrorDescription = "";
            } else {
                allErrorDescription = dinamicError.getAllErrorDescription();
            }
            dinamicAppMonitor2.alarm_commitFail("Dinamic", "BindData", sb, obj, allErrorDescription);
            if (dinamicError == null) {
                str2 = "";
            } else {
                str2 = dinamicError.getAllErrorDescription();
            }
            DinamicLog.remoteLoge(TAG, TAG, buildRemoteLogContent(str, "trackBindData", dinamicTemplate, str2));
        }
    }

    public void trackHandleEvent(String str, String str2, double d) {
        if (this.dinamicAppMonitor != null) {
            StringBuilder sb = new StringBuilder();
            appendStr(sb, WXGestureType.GestureInfo.POINTER_ID, str2);
            appendStr(sb, "module", str);
            this.dinamicAppMonitor.counter_commit("Dinamic", "HandleEvent", sb.toString(), d);
        }
    }

    public void trackFetchExactTemplate(String str, DTemplateManager.CacheStrategy cacheStrategy, DinamicTemplate dinamicTemplate, DinamicTemplate dinamicTemplate2, double d) {
        DinamicTemplate dinamicTemplate3 = dinamicTemplate;
        DinamicTemplate dinamicTemplate4 = dinamicTemplate2;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate4);
            appendStr(createArg, "originalTemplateVersion", dinamicTemplate3.version);
            appendStr(createArg, "module", str);
            appendStr(createArg, "cacheStrategy", cacheStrategy.equals(DTemplateManager.CacheStrategy.STRATEGY_DEFAULT) ? "0" : "1");
            boolean z = true;
            boolean z2 = false;
            if (dinamicTemplate4 != null) {
                if (dinamicTemplate.isPreset() && !dinamicTemplate2.isPreset()) {
                    z = false;
                }
                if (dinamicTemplate.isPreset() || dinamicTemplate3.version.equals(dinamicTemplate4.version)) {
                    z2 = z;
                }
            }
            if (z2) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "FetchExactTemplate", createArg.toString());
            } else {
                this.dinamicAppMonitor.alarm_commitFail("Dinamic", "FetchExactTemplate", createArg.toString(), (String) null, (String) null);
            }
            this.dinamicAppMonitor.counter_commit("Dinamic", "FetchExactTemplate", createArg.toString(), d);
        }
    }

    public void trackDownloadTemplate(String str, DinamicTemplate dinamicTemplate, boolean z, DinamicError dinamicError, double d) {
        String allErrorDescription;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate);
            appendStr(createArg, "module", str);
            if (z) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "DownloadTemplate", createArg.toString());
                this.dinamicAppMonitor.counter_commit("Dinamic", "DownloadTemplate", createArg.toString(), d);
                DinamicLog.remoteLogi(TAG, TAG, buildRemoteLogContent(str, "trackDownloadTemplate", dinamicTemplate, (String) null));
                return;
            }
            DinamicAppMonitor dinamicAppMonitor2 = this.dinamicAppMonitor;
            String sb = createArg.toString();
            String obj = (dinamicError == null || dinamicError.isEmpty()) ? "" : dinamicError.getErrorMap().keySet().toString();
            if (dinamicError == null) {
                allErrorDescription = "";
            } else {
                allErrorDescription = dinamicError.getAllErrorDescription();
            }
            dinamicAppMonitor2.alarm_commitFail("Dinamic", "DownloadTemplate", sb, obj, allErrorDescription);
            DinamicLog.remoteLoge(TAG, TAG, buildRemoteLogContent(str, "trackDownloadTemplate", dinamicTemplate, "downloadError"));
        }
    }

    public void trackWriteTemplate(String str, DinamicTemplate dinamicTemplate, boolean z, DinamicError dinamicError, double d) {
        String allErrorDescription;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate);
            appendStr(createArg, "module", str);
            if (z) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "WriteTemplate", createArg.toString());
                this.dinamicAppMonitor.counter_commit("Dinamic", "WriteTemplate", createArg.toString(), d);
                DinamicLog.remoteLogi(TAG, TAG, buildRemoteLogContent(str, "trackWriteTemplate", dinamicTemplate, (String) null));
                return;
            }
            DinamicAppMonitor dinamicAppMonitor2 = this.dinamicAppMonitor;
            String sb = createArg.toString();
            String obj = (dinamicError == null || dinamicError.isEmpty()) ? "" : dinamicError.getErrorMap().keySet().toString();
            if (dinamicError == null) {
                allErrorDescription = "";
            } else {
                allErrorDescription = dinamicError.getAllErrorDescription();
            }
            dinamicAppMonitor2.alarm_commitFail("Dinamic", "WriteTemplate", sb, obj, allErrorDescription);
            DinamicLog.remoteLoge(TAG, TAG, buildRemoteLogContent(str, "trackWriteTemplate", dinamicTemplate, "writeTemplateError"));
        }
    }

    public void trackReadTemplate(String str, DinamicTemplate dinamicTemplate, boolean z, DinamicError dinamicError, double d) {
        String allErrorDescription;
        if (this.dinamicAppMonitor != null) {
            StringBuilder createArg = createArg(dinamicTemplate);
            appendStr(createArg, "module", str);
            if (z) {
                this.dinamicAppMonitor.alarm_commitSuccess("Dinamic", "ReadTemplate", createArg.toString());
                this.dinamicAppMonitor.counter_commit("Dinamic", "ReadTemplate", createArg.toString(), d);
                return;
            }
            DinamicAppMonitor dinamicAppMonitor2 = this.dinamicAppMonitor;
            String sb = createArg.toString();
            String obj = (dinamicError == null || dinamicError.isEmpty()) ? "" : dinamicError.getErrorMap().keySet().toString();
            if (dinamicError == null) {
                allErrorDescription = "";
            } else {
                allErrorDescription = dinamicError.getAllErrorDescription();
            }
            dinamicAppMonitor2.alarm_commitFail("Dinamic", "ReadTemplate", sb, obj, allErrorDescription);
        }
    }

    private StringBuilder createArg(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return new StringBuilder();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("templateName=");
        sb.append(dinamicTemplate.name);
        sb.append(",templateVersion=");
        sb.append(dinamicTemplate.version);
        return sb;
    }

    private StringBuilder appendStr(StringBuilder sb, String str, String str2) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        if (sb.length() > 0) {
            sb.append(",");
        }
        sb.append(str);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(str2);
        return sb;
    }
}
