package com.alibaba.android.prefetchx.core.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFException;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.core.data.StorageInterface;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StorageWeex implements StorageInterface<String> {
    private static volatile StorageWeex instance;

    private StorageWeex() {
    }

    public static StorageWeex getInstance() {
        if (instance == null) {
            synchronized (StorageWeex.class) {
                if (instance == null) {
                    instance = new StorageWeex();
                }
            }
        }
        return instance;
    }

    public void save(final String str, String str2) throws PFException {
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter != null) {
                iWXStorageAdapter.setItem(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                    public void onReceived(Map<String, Object> map) {
                        if (map != null && !"success".equals(map.get("result"))) {
                            String str = "write to storage result is not success. " + str + ". map is" + map;
                            PFLog.Data.w(str, new Throwable[0]);
                            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str, new Object[0]);
                        }
                    }
                });
            }
        } catch (Exception e) {
            String str3 = "exception in save storage to weex. message is " + e.getMessage();
            PFLog.Data.w(str3, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str3, new Object[0]);
            throw new PFException((Throwable) e);
        }
    }

    @Nullable
    public String read(final String str) throws PFException {
        final String[] strArr = {null};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter == null) {
                return null;
            }
            iWXStorageAdapter.getItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(@NonNull Map<String, Object> map) {
                    if (!"success".equals(map.get("result"))) {
                        String str = "read to storage result is not success. " + str + ". map is" + map;
                        PFLog.Data.w(str, new Throwable[0]);
                        PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str, new Object[0]);
                    } else {
                        strArr[0] = map.get("data") != null ? map.get("data").toString() : "";
                    }
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await(TBToast.Duration.MEDIUM, TimeUnit.MICROSECONDS);
            return strArr[0];
        } catch (Exception e) {
            String str2 = "exception in read storage to weex. message is " + e.getMessage();
            PFLog.Data.w(str2, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str2, new Object[0]);
            throw new PFException((Throwable) e);
        }
    }

    public void readAsync(String str, final StorageInterface.Callback callback) throws PFException {
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter != null) {
                iWXStorageAdapter.getItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                    public void onReceived(@NonNull Map<String, Object> map) {
                        if (!"success".equals(map.get("result"))) {
                            String str = "read to storage result is not success" + map;
                            PFLog.Data.w(str, new Throwable[0]);
                            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str, new Object[0]);
                            callback.onError("502", str);
                            return;
                        }
                        callback.onSuccess(map.get("data") != null ? map.get("data").toString() : "");
                    }
                });
            }
        } catch (Exception e) {
            String str2 = "exception in read storage to weex. message is " + e.getMessage();
            PFLog.Data.w(str2, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str2, new Object[0]);
            callback.onError("502", str2);
        }
    }

    public boolean containsKey(final String str) throws PFException {
        final Boolean[] boolArr = {false};
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter == null) {
                return false;
            }
            iWXStorageAdapter.getAllKeys(new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(@NonNull Map<String, Object> map) {
                    if (!"success".equals(map.get("result"))) {
                        String str = "containsKey storage result is not success" + map;
                        PFLog.Data.w(str, new Throwable[0]);
                        PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str, new Object[0]);
                    } else {
                        Object obj = map.get("data");
                        if (obj instanceof ArrayList) {
                            boolArr[0] = Boolean.valueOf(((ArrayList) obj).contains(str));
                        }
                    }
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await(TBToast.Duration.MEDIUM, TimeUnit.MICROSECONDS);
            return boolArr[0].booleanValue();
        } catch (Exception e) {
            String str2 = "exception in containsKey storage in weex. message is " + e.getMessage();
            PFLog.Data.w(str2, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str2, new Object[0]);
            throw new PFException((Throwable) e);
        }
    }

    public void remove(String str) throws PFException {
        try {
            IWXStorageAdapter iWXStorageAdapter = WXSDKEngine.getIWXStorageAdapter();
            if (iWXStorageAdapter != null) {
                iWXStorageAdapter.removeItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                    public void onReceived(@NonNull Map<String, Object> map) {
                        if (!"success".equals(map.get("result"))) {
                            String str = "remove storage result is not success" + map;
                            PFLog.Data.w(str, new Throwable[0]);
                            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str, new Object[0]);
                        }
                    }
                });
            }
        } catch (Exception e) {
            String str2 = "exception in remove storage in weex. message is " + e.getMessage();
            PFLog.Data.w(str2, new Throwable[0]);
            if (PFUtil.isDebug()) {
                e.printStackTrace();
            }
            PFMonitor.Data.fail(PFConstant.Data.PF_DATA_SAVE_STATUS_STORAGE_ERROR, str2, new Object[0]);
            throw new PFException((Throwable) e);
        }
    }
}
