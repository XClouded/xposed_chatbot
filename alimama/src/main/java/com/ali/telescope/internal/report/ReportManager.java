package com.ali.telescope.internal.report;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Keep;
import com.ali.telescope.base.report.IReportBean;
import com.ali.telescope.base.report.IReportErrorBean;
import com.ali.telescope.base.report.IReportRawByteBean;
import com.ali.telescope.base.report.IReportStringBean;
import com.ali.telescope.internal.looper.Loopers;
import com.ali.telescope.util.FileUtils;
import com.ali.telescope.util.ProcessUtils;
import com.ali.telescope.util.StringUtils;
import com.ali.telescope.util.TelescopeLog;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.util.HashMap;

@Keep
public class ReportManager {
    public static final String LOG_PATH = "log";
    public static final String TAG = "ReportManager";
    /* access modifiers changed from: private */
    public static byte initState = 1;
    public static long session;
    private volatile boolean isInited;

    /* access modifiers changed from: private */
    public native void appendBytesBody(short s, long j, byte[] bArr);

    /* access modifiers changed from: private */
    public native void appendNoBody(short s, long j);

    /* access modifiers changed from: private */
    public native void appendStringBody(short s, long j, String str);

    /* access modifiers changed from: private */
    public native boolean init(String str, String str2, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3);

    private native void trim(String str, String str2);

    private ReportManager() {
        this.isInited = false;
    }

    public static final ReportManager getInstance() {
        return SingleInstanceHolder.sInstance;
    }

    private static final class SingleInstanceHolder {
        /* access modifiers changed from: private */
        public static final ReportManager sInstance = new ReportManager();

        private SingleInstanceHolder() {
        }
    }

    public void append(final IReportBean iReportBean) {
        if (initState == 1) {
            Log.e(TAG, "Appending, but so was loaded failed!");
        } else {
            runInReportThread(new Runnable() {
                public void run() {
                    try {
                        if (iReportBean instanceof IReportStringBean) {
                            TelescopeLog.v(ReportManager.TAG, "send type: 0x" + Integer.toHexString(iReportBean.getType()) + ", time:" + iReportBean.getTime() + ", Body:" + ((IReportStringBean) iReportBean).getBody());
                            if (!TextUtils.isEmpty(((IReportStringBean) iReportBean).getBody())) {
                                ReportManager.this.appendStringBody(iReportBean.getType(), iReportBean.getTime(), ((IReportStringBean) iReportBean).getBody());
                            }
                        } else if (iReportBean instanceof IReportRawByteBean) {
                            TelescopeLog.v(ReportManager.TAG, "send type: 0x" + Integer.toHexString(iReportBean.getType()) + ", time:" + iReportBean.getTime() + ", Body:" + ((IReportRawByteBean) iReportBean).getBody());
                            if (((IReportRawByteBean) iReportBean).getBody() != null) {
                                ReportManager.this.appendBytesBody(iReportBean.getType(), iReportBean.getTime(), ((IReportRawByteBean) iReportBean).getBody());
                            }
                        } else if (!(iReportBean instanceof IReportErrorBean)) {
                            TelescopeLog.e(ReportManager.TAG, "You should pick a right concrete Bean interface, type: 0x" + Integer.toHexString(iReportBean.getType()));
                            ReportManager.this.appendNoBody(iReportBean.getType(), iReportBean.getTime());
                        } else if (((IReportErrorBean) iReportBean).getBody() != null) {
                            ErrorReportManager.adapter((IReportErrorBean) iReportBean);
                        }
                    } catch (Throwable th) {
                        TelescopeLog.e("native method not found.\n" + th, new Object[0]);
                    }
                }
            });
        }
    }

    private void runInReportThread(Runnable runnable) {
        if (Thread.currentThread() == Loopers.getReportLooper().getThread()) {
            runnable.run();
        } else {
            Loopers.getReportHandler().post(runnable);
        }
    }

    public void initSuperLog(final Application application, final HashMap<String, String> hashMap, final HashMap<String, String> hashMap2) {
        if (!this.isInited) {
            this.isInited = true;
            runInReportThread(new Runnable() {
                public void run() {
                    HashMap<String, String> typeDescriptor = ProtocolConstants.getTypeDescriptor();
                    String pathPrefix = ReportManager.getPathPrefix(application);
                    String pathCachPrefix = ReportManager.getPathCachPrefix(application);
                    ReportManager.session = System.currentTimeMillis();
                    if (ReportManager.this.init(pathCachPrefix + File.separator + ReportManager.session, pathPrefix + File.separator + ReportManager.session, hashMap, hashMap2, typeDescriptor)) {
                        byte unused = ReportManager.initState = (byte) 0;
                    } else {
                        byte unused2 = ReportManager.initState = (byte) 2;
                    }
                }
            });
        }
    }

    public void trimHotdataBeforeUpload(String str, String str2) {
        if (initState == 1) {
            Log.e(TAG, "Triming, but so was loaded failed!");
        } else if (!StringUtils.isEmpty(str) && !StringUtils.isEmpty(str2)) {
            trim(str, str2);
        }
    }

    public static String getPathPrefix(Context context) {
        String replace = ProcessUtils.getProcessName(context).replace(Operators.CONDITION_IF_MIDDLE, '.');
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        return FileUtils.getTelescopeCachePath(context, "log" + File.separator + replace);
    }

    public static String getPathCachPrefix(Context context) {
        String replace = ProcessUtils.getProcessName(context).replace(Operators.CONDITION_IF_MIDDLE, '.');
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        return FileUtils.getTelescopeDataPath(context, "log" + File.separator + replace);
    }

    static {
        try {
            System.loadLibrary("superlog");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
