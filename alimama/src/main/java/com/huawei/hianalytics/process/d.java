package com.huawei.hianalytics.process;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.e.c;
import com.huawei.hianalytics.e.e;
import com.huawei.hianalytics.f.e.a;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.f;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class d implements HiAnalyticsInstance {
    public e a;
    private String b;

    public d(String str) {
        this.b = str;
        this.a = new e(str);
    }

    private c a(int i) {
        switch (i) {
            case 0:
                return this.a.b();
            case 1:
                return this.a.a();
            case 2:
                return this.a.c();
            case 3:
                return this.a.d();
            default:
                return null;
        }
    }

    private boolean b(int i) {
        if (i != 2) {
            c a2 = a(i);
            if (a2 != null && !TextUtils.isEmpty(a2.g())) {
                return true;
            }
            b.c("HiAnalytics/event", "verifyURL(): URL check failed. type: " + i);
            return false;
        } else if ("_default_config_tag".equals(this.b)) {
            return true;
        } else {
            b.c("HiAnalytics/event", "verifyURL(): type: preins. Only default config can report Pre-install data.");
            return false;
        }
    }

    public void a(HiAnalyticsConfig hiAnalyticsConfig) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setMaintConf() is executed.TAG : " + this.b);
        if (hiAnalyticsConfig == null) {
            b.c("HiAnalytics/event", "HiAnalyticsInstanceImpl.setMaintConf(): config for maint is null!");
            this.a.a((c) null);
            return;
        }
        this.a.a(hiAnalyticsConfig.cfgData);
    }

    public void b(HiAnalyticsConfig hiAnalyticsConfig) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setDiffConf() is executed.TAG : " + this.b);
        if (hiAnalyticsConfig == null) {
            b.c("HiAnalytics/event", "HiAnalyticsInstanceImpl.setDiffConf(): config for diffPrivacy is null!");
            this.a.d((c) null);
            return;
        }
        this.a.d(hiAnalyticsConfig.cfgData);
    }

    public void c(HiAnalyticsConfig hiAnalyticsConfig) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setOperConf() is executed.TAG: " + this.b);
        if (hiAnalyticsConfig == null) {
            this.a.b((c) null);
            b.c("HiAnalytics/event", "HiAnalyticsInstanceImpl.setOperConf(): config for oper is null!");
            return;
        }
        this.a.b(hiAnalyticsConfig.cfgData);
    }

    public void clearData() {
        a.b().d(this.b);
    }

    public void d(HiAnalyticsConfig hiAnalyticsConfig) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setPreInstallConf() is executed.TAG: " + this.b);
        if (hiAnalyticsConfig == null) {
            b.c("HiAnalytics/event", "HiAnalyticsInstanceImpl.setPreInstallConf(): config for PRE-INSTALL is null!");
            this.a.c((c) null);
            return;
        }
        this.a.c(hiAnalyticsConfig.cfgData);
    }

    public void onBackground(long j) {
        String str;
        String str2;
        b.b("HiAnalytics/event", "onBackground() is executed.TAG : %s", this.b);
        c b2 = this.a.b();
        if (b2 == null) {
            str2 = "HiAnalytics/event";
            str = "No operConf";
        } else if (b2.d()) {
            a.a().b(this.b, j);
            return;
        } else {
            str2 = "HiAnalytics/event";
            str = "No Session switch is set.";
        }
        b.c(str2, str);
    }

    public void onEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onEvent(int type, String eventId, Map<String, String> mapValue) is execute.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
        if (f.a(str) || !b(i)) {
            b.c("HiAnalytics/event", "onEvent() parameters check fail. Nothing will be recorded.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
            return;
        }
        if (!f.a((Map<String, String>) linkedHashMap)) {
            b.c("HiAnalytics/event", "onEvent() parameter mapValue will be cleared.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
            linkedHashMap = null;
        }
        b.a().a(this.b, i, str, linkedHashMap);
    }

    @Deprecated
    public void onEvent(Context context, String str, String str2) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onEvent(eventId, mapValue) is execute.TAG : " + this.b);
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onevent ");
        } else if (f.a(str) || !b(0)) {
            b.c("HiAnalytics/event", "onEvent() parameters check fail. Nothing will be recorded.TAG: " + this.b);
        } else {
            if (!f.a("value", str2, 65536)) {
                b.c("HiAnalytics/event", "onEvent() parameter VALUE is overlong, content will be cleared.TAG: " + this.b);
                str2 = "";
            }
            b.a().a(this.b, context, str, str2);
        }
    }

    public void onEvent(String str, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onEvent(String eventId, Map<String, String> mapValue) is execute.TAG: " + this.b);
        if (f.a(str) || !b(0)) {
            b.c("HiAnalytics/event", "onEvent() parameters check fail. Nothing will be recorded.TAG: " + this.b);
            return;
        }
        if (!f.a((Map<String, String>) linkedHashMap)) {
            b.c("HiAnalytics/event", "onEvent() parameter mapValue will be cleared.TAG: " + this.b);
            linkedHashMap = null;
        }
        b.a().a(this.b, 0, str, linkedHashMap);
    }

    public void onForeground(long j) {
        String str;
        String str2;
        b.b("HiAnalytics/event", "onForeground() is executed。TAG : %s", this.b);
        c b2 = this.a.b();
        if (b2 == null) {
            str2 = "HiAnalytics/event";
            str = "No operConf";
        } else if (b2.d()) {
            a.a().c(this.b, j);
            return;
        } else {
            str2 = "HiAnalytics/event";
            str = "No Session switch is set.";
        }
        b.c(str2, str);
    }

    public void onPause(Context context) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onPause() is execute.TAG: " + this.b);
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onPause! Nothing will be recorded.TAG: " + this.b);
        } else if (!b(0)) {
            b.c("HiAnalytics/event", "onResume() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else {
            b.a().a(this.b, context);
        }
    }

    public void onPause(Context context, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onPause(context,map) is execute.TAG: " + this.b);
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onPause! Nothing will be recorded.");
        } else if (!b(0)) {
            b.c("HiAnalytics/event", "onResume() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else {
            if (!f.a((Map<String, String>) linkedHashMap)) {
                b.c("HiAnalytics/event", "onPause() parameter mapValue will be cleared.TAG: " + this.b);
                linkedHashMap = null;
            }
            b.a().a(this.b, context, linkedHashMap);
        }
    }

    public void onPause(String str, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onPause(viewName,map) is execute.TAG: " + this.b);
        if (!b(0)) {
            b.c("HiAnalytics/event", "onPause() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else if (TextUtils.isEmpty(str) || !f.a("viewName", str, "[a-zA-Z_][a-zA-Z0-9. _-]{0,255}")) {
            b.c("HiAnalytics/event", "onPause() parameter viewName verify failed. Nothing will be recorded.TAG: " + this.b);
        } else {
            if (!f.a((Map<String, String>) linkedHashMap)) {
                b.c("HiAnalytics/event", "onPause() parameter mapValue will be cleared.TAG: " + this.b);
                linkedHashMap = null;
            }
            b.a().a(this.b, str, linkedHashMap);
        }
    }

    public void onReport(int i) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onReport() is execute.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
        b.a().a(this.b, i);
    }

    @Deprecated
    public void onReport(Context context, int i) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onReport(Context context) is execute.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onreport!");
        } else {
            b.a().a(this.b, context, i);
        }
    }

    public void onResume(Context context) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onResume() is execute.TAG: " + this.b);
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onResume! Nothing will be recorded.");
        } else if (!b(0)) {
            b.c("HiAnalytics/event", "onResume() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else {
            b.a().b(this.b, context);
        }
    }

    public void onResume(Context context, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onResume(context,map) is execute.TAG: " + this.b);
        if (context == null) {
            b.c("HiAnalytics/event", "context is null in onResume! Nothing will be recorded.");
        } else if (!b(0)) {
            b.c("HiAnalytics/event", "onResume() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else {
            if (!f.a((Map<String, String>) linkedHashMap)) {
                b.c("HiAnalytics/event", "onResume() parameter mapValue will be cleared.TAG: " + this.b);
                linkedHashMap = null;
            }
            b.a().b(this.b, context, linkedHashMap);
        }
    }

    public void onResume(String str, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onResume(viewname,map) is execute.TAG: " + this.b);
        if (!b(0)) {
            b.c("HiAnalytics/event", "onResume() URL check fail. Nothing will be recorded.TAG: " + this.b);
        } else if (TextUtils.isEmpty(str) || !f.a("viewName", str, "[a-zA-Z_][a-zA-Z0-9. _-]{0,255}")) {
            b.c("HiAnalytics/event", "onResume() parameter viewName verify failed. Nothing will be recorded.TAG: " + this.b);
        } else {
            if (!f.a((Map<String, String>) linkedHashMap)) {
                b.c("HiAnalytics/event", "onResume() parameter mapValue will be cleared.TAG: " + this.b);
                linkedHashMap = null;
            }
            b.a().b(this.b, str, linkedHashMap);
        }
    }

    public void onStreamEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap) {
        b.b("HiAnalytics/event", "HiAnalyticsInstance.onStreamEvent() is execute.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
        if (f.a(str) || !b(i)) {
            b.c("HiAnalytics/event", "onEventIM() parameters check fail. Nothing will be recorded.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
            return;
        }
        if (!f.a((Map<String, String>) linkedHashMap)) {
            b.c("HiAnalytics/event", "onEventIM() parameter mapValue will be cleared.TAG: %s,TYPE: %d", this.b, Integer.valueOf(i));
            linkedHashMap = null;
        }
        b.a().b(this.b, i, str, linkedHashMap);
    }

    public void refresh(int i, HiAnalyticsConfig hiAnalyticsConfig) {
        HiAnalyticsConfig hiAnalyticsConfig2;
        if (hiAnalyticsConfig == null) {
            b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.refresh(). Parameter config is null.TAG : %s , TYPE : %d", this.b, Integer.valueOf(i));
            hiAnalyticsConfig2 = null;
        } else {
            hiAnalyticsConfig2 = new HiAnalyticsConfig(hiAnalyticsConfig);
        }
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.refresh() is executed.TAG : %s , TYPE : %d", this.b, Integer.valueOf(i));
        switch (i) {
            case 0:
                c(hiAnalyticsConfig2);
                a.a().a(this.b);
                return;
            case 1:
                a(hiAnalyticsConfig2);
                return;
            case 2:
                d(hiAnalyticsConfig2);
                return;
            case 3:
                b(hiAnalyticsConfig2);
                return;
            default:
                b.c("HiAnalytics/event", "refresh(): HiAnalyticsType can only be OPERATION ,MAINTAIN or DIFF_PRIVACY.");
                return;
        }
    }

    public void setCommonProp(int i, Map<String, String> map) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setCommonProp() is executed.TAG : %s , TYPE : %d", this.b, Integer.valueOf(i));
        if (!f.a(map)) {
            b.c("HiAnalytics/event", "setCommonProp() parameter mapValue will be cleared.");
            return;
        }
        JSONObject jSONObject = new JSONObject(map);
        c a2 = a(i);
        if (a2 == null) {
            b.c("HiAnalytics/event", "setCommonProp(): No related config found.");
        } else {
            a2.e(String.valueOf(jSONObject));
        }
    }

    public void setOAID(int i, String str) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setStrOAID() is executed.TAG : " + this.b);
        c a2 = a(i);
        if (a2 == null) {
            b.c("HiAnalytics/event", "setOAID(): No related config found.type : %d", Integer.valueOf(i));
            return;
        }
        if (!f.a("oaid", str, 4096)) {
            str = "";
        }
        a2.c(str);
    }

    public void setOAIDTrackingFlag(int i, boolean z) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setOAIDTrackingFlag() is executed.TAG : %s , TYPE : %d", this.b, Integer.valueOf(i));
        c a2 = a(i);
        if (a2 == null) {
            b.c("HiAnalytics/event", "setOAIDTrackingFlag(): No related config found.type : %d", Integer.valueOf(i));
        } else {
            a2.d(z ? "true" : "false");
        }
    }

    public void setUpid(int i, String str) {
        b.b("HiAnalytics/event", "HiAnalyticsInstanceImpl.setUpid() is executed.TAG : " + this.b);
        c a2 = a(i);
        if (a2 == null) {
            b.c("HiAnalytics/event", "setUpid(): No related config found.type : %d ", Integer.valueOf(i));
            return;
        }
        if (!f.a("upid", str, 4096)) {
            str = "";
        }
        a2.f(str);
    }
}
