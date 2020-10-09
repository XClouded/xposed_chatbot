package com.alimamaunion.base.configcenter;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import com.alimama.unionwl.unwcache.ICache;
import com.alimama.unionwl.unwcache.MemoryCache;
import com.alimama.unionwl.unwcache.MemoryResult;
import com.alimama.unionwl.unwcache.SimpleDiskCache;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class DefaultConfigCenterCache implements IConfigCenterCache {
    private Application mApplication;
    /* access modifiers changed from: private */
    public ICache.IMemoryCache mMemoryCache;

    public DefaultConfigCenterCache(Application application) {
        init(application);
    }

    private void init(Application application) {
        this.mApplication = application;
        this.mMemoryCache = new MemoryCache(1024);
        SimpleDiskCache.init(application);
    }

    public void remove(String str) {
        this.mMemoryCache.removeDataFromMemory(str);
        SimpleDiskCache.getInstance().removeDataFromDisk(str);
    }

    public ConfigData read(String str) {
        MemoryResult memoryResult;
        MemoryResult dataFromCache = getDataFromCache(str);
        if (dataFromCache != null) {
            byte[] bArr = dataFromCache.data;
            memoryResult = dataFromCache;
        } else {
            ByteArrayOutputStream readAssertByte = readAssertByte(this.mApplication, "config-center/" + str);
            if (readAssertByte != null) {
                MemoryResult memoryResult2 = new MemoryResult(readAssertByte.toByteArray(), (String) null);
                try {
                    readAssertByte.close();
                } catch (IOException unused) {
                }
                memoryResult = memoryResult2;
            } else {
                memoryResult = new MemoryResult((byte[]) null, (String) null);
            }
        }
        String str2 = "0";
        String str3 = "0";
        boolean z = true;
        if (!TextUtils.isEmpty(memoryResult.extra)) {
            try {
                SafeJSONObject safeJSONObject = new SafeJSONObject(memoryResult.extra);
                String optString = safeJSONObject.optString("lastModified", "0");
                try {
                    String optString2 = safeJSONObject.optString("updateInterval", "0");
                    try {
                        z = safeJSONObject.optBoolean("isChanged", true);
                    } catch (Exception unused2) {
                    }
                    str3 = optString2;
                } catch (Exception unused3) {
                }
                str2 = optString;
            } catch (Exception unused4) {
            }
        }
        return new ConfigData(memoryResult.data, str2, str3, z);
    }

    public void write(final String str, final ConfigData configData) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            public void call(Subscriber<? super Object> subscriber) {
                SafeJSONObject safeJSONObject = new SafeJSONObject();
                safeJSONObject.put("lastModified", configData.lastModified);
                safeJSONObject.put("updateInterval", configData.updateInterval);
                safeJSONObject.put("isChanged", configData.isChanged);
                DefaultConfigCenterCache.this.mMemoryCache.putDataToMemory(str, safeJSONObject.toString(), configData.data);
                SimpleDiskCache.getInstance().putDataToDisk(str, safeJSONObject.toString(), configData.data);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private MemoryResult getDataFromCache(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        MemoryResult dateFromMemory = this.mMemoryCache.getDateFromMemory(str);
        if (dateFromMemory != null) {
            return dateFromMemory;
        }
        SimpleDiskCache.SimpleDiskResult dataFromDisk = SimpleDiskCache.getInstance().getDataFromDisk(str, true, true);
        if (!(dataFromDisk == null || dataFromDisk.data == null)) {
            dateFromMemory = new MemoryResult(dataFromDisk.data, dataFromDisk.extra);
        }
        if (dateFromMemory == null) {
            return dateFromMemory;
        }
        this.mMemoryCache.putDataToMemory(str, dataFromDisk.extra, dateFromMemory.data);
        return dateFromMemory;
    }

    public static ByteArrayOutputStream readAssertByte(Context context, String str) {
        try {
            if (str.startsWith(File.separator)) {
                str = str.substring(File.separator.length());
            }
            DataInputStream dataInputStream = new DataInputStream(context.getAssets().open(str));
            byte[] bArr = new byte[dataInputStream.available()];
            dataInputStream.readFully(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bArr);
            dataInputStream.close();
            return byteArrayOutputStream;
        } catch (Exception unused) {
            return null;
        }
    }
}
