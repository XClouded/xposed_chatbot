package com.alibaba.android.umbrella.link.util;

import android.content.Context;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.umbrella.link.UMStringUtils;
import com.alibaba.fastjson.JSON;
import com.ta.utdid2.device.UTUtdid;
import com.uc.webview.export.cyclone.ErrorCode;
import com.ut.mini.UTPageHitHelper;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.mtop.domain.MtopResponse;

public final class UMLinkLogUtils {
    private static final String DEFAULT_GROUP = "default_group";
    private static final String DEFAULT_THREAD = "default_thread";
    private static final long TS_2019_01_01_000 = 1546272000433L;

    public static String getThreadId() {
        Thread currentThread = Thread.currentThread();
        String name = UMStringUtils.isEmpty(currentThread.getName()) ? DEFAULT_THREAD : currentThread.getName();
        long id = currentThread.getId();
        ThreadGroup threadGroup = currentThread.getThreadGroup();
        String name2 = threadGroup != null ? UMStringUtils.isEmpty(threadGroup.getName()) ? DEFAULT_GROUP : threadGroup.getName() : DEFAULT_GROUP;
        return name2 + "|" + name + "|" + id;
    }

    public static String getPageName() {
        return UTPageHitHelper.getInstance().getCurrentPageName();
    }

    public static String getLLTimestamp() {
        return String.valueOf(System.currentTimeMillis() - TS_2019_01_01_000);
    }

    public static String getUtdid() {
        try {
            return UTUtdid.instance((Context) null).getValue();
        } catch (Throwable unused) {
            return String.valueOf(SystemClock.currentThreadTimeMillis());
        }
    }

    public static String getTraceIds(MtopResponse mtopResponse) {
        Map<String, List<String>> headerFields;
        if (mtopResponse == null || (headerFields = mtopResponse.getHeaderFields()) == null) {
            return "";
        }
        String oneHeader = getOneHeader(headerFields, "mtop-buytraceid");
        if (UMStringUtils.isNotEmpty(oneHeader)) {
            return oneHeader;
        }
        String oneHeader2 = getOneHeader(headerFields, "x-eagleeye-id");
        if (UMStringUtils.isNotEmpty(oneHeader2)) {
            return oneHeader2;
        }
        String oneHeader3 = getOneHeader(headerFields, HttpHeaderConstant.EAGLE_TRACE_ID);
        if (UMStringUtils.isNotEmpty(oneHeader3)) {
            return oneHeader3;
        }
        String oneHeader4 = getOneHeader(headerFields, "EagleEye-TraceId");
        return UMStringUtils.isNotEmpty(oneHeader4) ? oneHeader4 : "";
    }

    private static String getOneHeader(@NonNull Map<String, List<String>> map, @NonNull String str) {
        List list = map.get(str);
        if (list == null || list.isEmpty()) {
            return "";
        }
        String valueOf = String.valueOf(list.get(0));
        return UMStringUtils.isNotEmpty(valueOf) ? valueOf : "";
    }

    public static String ensureText(@Nullable String str) {
        return UMStringUtils.isEmpty(str) ? "" : str;
    }

    public static void main(String[] strArr) {
        Calendar instance = Calendar.getInstance();
        instance.set(ErrorCode.UCSERVICE_UCDEX_IMPL_NOT_FOUND, 0, 1, 0, 0, 0);
        System.out.println(instance.getTime().toString());
        System.out.println(instance.getTimeInMillis());
    }

    public static String toUnifiedString(@Nullable Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        return JSON.toJSONString(obj);
    }
}
