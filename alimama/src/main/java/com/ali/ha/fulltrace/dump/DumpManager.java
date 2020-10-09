package com.ali.ha.fulltrace.dump;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.ali.ha.fulltrace.FTHeader;
import com.ali.ha.fulltrace.FileUtils;
import com.ali.ha.fulltrace.FulltraceGlobal;
import com.ali.ha.fulltrace.IReportEvent;
import com.ali.ha.fulltrace.IReportRawByteEvent;
import com.ali.ha.fulltrace.ProtocolConstants;
import com.ali.ha.fulltrace.logger.Logger;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.util.HashMap;

public class DumpManager {
    public static final String LOG_PATH = "log";
    public static final String TAG = "FULLTRACE";
    private static volatile byte initState = -1;
    public static long session;
    private volatile boolean isInited;

    /* access modifiers changed from: private */
    public native void appendBytesBody(short s, long j, byte[] bArr);

    /* access modifiers changed from: private */
    public native void appendNoBody(short s, long j);

    private native boolean init(String str, String str2, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3);

    private native void trim(String str, String str2);

    private DumpManager() {
        this.isInited = false;
    }

    public static final DumpManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    private static final class SingleInstanceHolder {
        /* access modifiers changed from: private */
        public static final DumpManager INSTANCE = new DumpManager();

        private SingleInstanceHolder() {
        }
    }

    public void append(final IReportEvent iReportEvent) {
        if (initState != 0) {
            Logger.e(TAG, "Appending, but so was loaded failed!");
        } else if (!this.isInited) {
            Logger.e(TAG, "Appending, but didn't initialize!");
        } else {
            runInReportThread(new Runnable() {
                public void run() {
                    try {
                        if (iReportEvent instanceof IReportRawByteEvent) {
                            byte[] body = ((IReportRawByteEvent) iReportEvent).getBody();
                            long time = iReportEvent.getTime();
                            short type = iReportEvent.getType();
                            Logger.d(DumpManager.TAG, "send rawBody type: 0x" + Integer.toHexString(type) + ", time:" + time + ", Body:" + body);
                            if (body != null) {
                                DumpManager.this.appendBytesBody(type, time, body);
                            }
                        } else if (iReportEvent instanceof IReportEvent) {
                            Logger.d(DumpManager.TAG, "send nobody type: 0x" + Integer.toHexString(iReportEvent.getType()));
                            DumpManager.this.appendNoBody(iReportEvent.getType(), iReportEvent.getTime());
                        }
                    } catch (Throwable th) {
                        Logger.e("native method not found.\n" + th, new Object[0]);
                    }
                }
            });
        }
    }

    private void runInReportThread(Runnable runnable) {
        try {
            FulltraceGlobal.instance().dumpHandler().post(runnable);
        } catch (Exception unused) {
        }
    }

    public void initTraceLog(Application application, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        if (!this.isInited) {
            this.isInited = true;
            if (initState != 0) {
                Logger.e(TAG, "initing, but so was loaded failed!");
                return;
            }
            HashMap<String, String> typeDescriptor = ProtocolConstants.getTypeDescriptor();
            String pathPrefix = getPathPrefix(application);
            String pathCachPrefix = getPathCachPrefix(application);
            session = System.currentTimeMillis();
            if (!init(pathCachPrefix + File.separator + session, pathPrefix + File.separator + session, hashMap, hashMap2, typeDescriptor)) {
                initState = 2;
            }
        }
    }

    public void trimHotdataBeforeUpload(String str, String str2) {
        if (initState != 0) {
            Logger.e(TAG, "Triming, but so was loaded failed!");
        } else if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            trim(str, str2);
        }
    }

    public static String getPathPrefix(Context context) {
        String replace = FTHeader.processName.replace(Operators.CONDITION_IF_MIDDLE, '.');
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        return FileUtils.getFulltraceCachePath(context, "log" + File.separator + replace);
    }

    public static String getPathCachPrefix(Context context) {
        String replace = FTHeader.processName.replace(Operators.CONDITION_IF_MIDDLE, '.');
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        return FileUtils.getFulltraceDataPath(context, "log" + File.separator + replace);
    }

    static {
        try {
            System.loadLibrary("fulltrace");
            initState = 0;
        } catch (Throwable th) {
            th.printStackTrace();
            initState = 1;
        }
    }
}
