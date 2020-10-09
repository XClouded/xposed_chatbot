package com.taobao.android.dinamicx.template.loader;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXIOUtils;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DXFileManager {
    private static File filePath;
    private final LruCache<String, byte[]> fileCache = new LruCache<>(50);

    public String getAssetsPath() {
        return DXTemplateNamePathUtil.ASSET_DIR;
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DXFileManager INSTANCE = new DXFileManager();

        private SingletonHolder() {
        }
    }

    public static DXFileManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void initFilePath(@NonNull Context context) {
        if (context == null) {
            DXRemoteLog.remoteLoge("DinamicX_File", "DXFileManager", "context is null");
        }
        if (filePath == null || !filePath.exists()) {
            filePath = new File(context.getFilesDir(), DXTemplateNamePathUtil.DEFAULT_ROOT_DIR);
            if (!filePath.exists() && !filePath.mkdirs()) {
                filePath.mkdirs();
            }
        }
    }

    public String getFilePath() {
        if (filePath == null) {
            return "";
        }
        return filePath.getAbsolutePath();
    }

    public byte[] load(String str, DXRuntimeContext dXRuntimeContext) {
        List<DXError.DXErrorInfo> list;
        byte[] bArr;
        long nanoTime = System.nanoTime();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            synchronized (this.fileCache) {
                bArr = this.fileCache.get(str);
                if ((bArr == null || bArr.length == 0) && (bArr = DXIOUtils.readAllBytes(str)) != null && bArr.length > 0) {
                    this.fileCache.put(str, bArr);
                }
                trackerPerform(DXMonitorConstant.DX_MONITOR_TEMPLATE_READ, System.nanoTime() - nanoTime);
            }
            return bArr;
        } catch (IOException e) {
            if (dXRuntimeContext == null || dXRuntimeContext.getDxError() == null || (list = dXRuntimeContext.getDxError().dxErrorInfoList) == null) {
                return null;
            }
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_TEMPLATE, DXMonitorConstant.DX_MONITOR_TEMPLATE_READ, DXError.DX_TEMPLATE_IO_READ_ERROR);
            if (e instanceof FileNotFoundException) {
                dXErrorInfo.reason = "fileNotFound " + str;
            } else {
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(e);
            }
            list.add(dXErrorInfo);
            return null;
        }
    }

    public boolean save(String str, byte[] bArr) {
        long nanoTime = System.nanoTime();
        boolean writeTemplateToFile = DXIOUtils.writeTemplateToFile(str, bArr);
        if (writeTemplateToFile) {
            trackerPerform(DXMonitorConstant.DX_MONITOR_TEMPLATE_WRITE, System.nanoTime() - nanoTime);
        }
        return writeTemplateToFile;
    }

    public void putFileCache(String str, byte[] bArr) {
        if (!TextUtils.isEmpty(str) && bArr != null) {
            synchronized (this.fileCache) {
                this.fileCache.put(str, bArr);
            }
        }
    }

    public void clearFileCache() {
        synchronized (this.fileCache) {
            this.fileCache.evictAll();
        }
    }

    private void trackerPerform(String str, long j) {
        DXAppMonitor.trackerPerform(2, "DinamicX_File", DXMonitorConstant.DX_MONITOR_TEMPLATE, str, (DXTemplateItem) null, (Map<String, String>) null, (double) j, true);
    }
}
