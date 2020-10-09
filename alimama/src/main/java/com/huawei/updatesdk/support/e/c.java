package com.huawei.updatesdk.support.e;

import android.os.SystemProperties;
import android.text.TextUtils;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.huawei.updatesdk.sdk.a.c.a.a.a;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import java.util.HashMap;
import java.util.Map;

public final class c {
    private static final Map<Integer, String> a = new HashMap();
    private static c d = new c();
    private int b;
    private String c;

    static {
        a.put(1, "1.0");
        a.put(2, "1.5");
        a.put(3, "1.6");
        a.put(4, "2.0");
        a.put(5, "2.0");
        a.put(6, "2.3");
        a.put(7, DXMonitorConstant.DX_MONITOR_VERSION);
        a.put(8, "3.0.5");
        a.put(8, "3.1");
        a.put(9, "4.0");
        a.put(10, "4.1");
        a.put(11, DispatchConstants.VER_CODE);
        a.put(12, "5.1");
    }

    private c() {
        this.b = 0;
        this.c = "";
        this.b = d();
        if (this.b == 0) {
            this.b = e();
        }
        this.c = f();
        a.a("EMUISupportUtil", "emuiVersion:" + this.b + ",emuiVersionName:" + this.c);
    }

    public static c a() {
        return d;
    }

    private String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String[] split = str.split("_");
        return split.length == 2 ? split[1] : "";
    }

    private int d() {
        return SystemProperties.getInt("ro.build.hw_emui_api_level", 0);
    }

    private int e() {
        String a2 = a(SystemProperties.get("ro.build.version.emui", ""));
        if (TextUtils.isEmpty(a2)) {
            return 0;
        }
        for (Map.Entry next : a.entrySet()) {
            if (a2.equals(next.getValue())) {
                return ((Integer) next.getKey()).intValue();
            }
        }
        return 0;
    }

    private String f() {
        String str = a.get(Integer.valueOf(this.b));
        return str == null ? "" : str;
    }

    public int b() {
        return this.b;
    }

    public String c() {
        return this.c;
    }
}
