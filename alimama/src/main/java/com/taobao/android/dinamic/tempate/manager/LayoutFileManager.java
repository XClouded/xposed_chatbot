package com.taobao.android.dinamic.tempate.manager;

import android.content.Context;
import android.util.Log;
import android.util.LruCache;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.parser.IOUtils;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.tempate.manager.TemplateCache;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class LayoutFileManager {
    private static final int BYTE_MEM_CACHE_SIZE = 16;
    private static final long FILE_CAPACITY = 2097152;
    private static final String TAG = "LayoutFileManager";
    private Context context;
    private String dbName = "default_layout.db";
    protected LruCache<String, DinamicTemplate> degradeCache;
    private String rootDirName = "default_layout";
    private TemplateCache templateCache;

    public LayoutFileManager(@NonNull Context context2, String str) {
        if (context2 != null) {
            this.context = context2.getApplicationContext();
        }
        this.rootDirName = str + "_layout";
        this.dbName = str + "_layout.db";
        this.degradeCache = new LruCache<>(16);
        this.templateCache = new TemplateCache.Builder().withContext(context2).withDbName(this.dbName).withRootDirName(this.rootDirName).withMemCacheSize(16).withFileCapacity(2097152).build();
    }

    public void setHttpLoader(TemplateCache.HttpLoader httpLoader) {
        if (httpLoader != null) {
            this.templateCache.httpLoader = httpLoader;
        }
    }

    private TemplateCache getTemplateCache() {
        return this.templateCache;
    }

    public byte[] getTemplateById(DinamicTemplate dinamicTemplate, String str, String str2, TemplatePerfInfo templatePerfInfo) {
        return getTemplateCache().getTemplateById(dinamicTemplate, str, str2, templatePerfInfo);
    }

    public boolean isLocalLayoutFileExists(@NonNull String str) {
        return getTemplateCache().memCache.get(str) != null || new File(this.templateCache.getRootDir(), str).exists();
    }

    public DinamicTemplate fetchDegradeFile(DinamicTemplate dinamicTemplate) {
        final String str = dinamicTemplate.name;
        try {
            final int intValue = Integer.valueOf(dinamicTemplate.version).intValue();
            DinamicTemplate dinamicTemplate2 = this.degradeCache.get(str);
            if (dinamicTemplate2 != null) {
                return dinamicTemplate2;
            }
            final DinamicTemplate dinamicTemplate3 = new DinamicTemplate();
            String[] list = this.templateCache.getRootDir().list(new FilenameFilter() {
                int tempVersion = -1;

                public boolean accept(File file, String str) {
                    int lastIndexOf;
                    if (!str.startsWith(str) || (lastIndexOf = str.lastIndexOf("_")) == -1) {
                        return false;
                    }
                    try {
                        int intValue = Integer.valueOf(str.substring(lastIndexOf + 1)).intValue();
                        if (intValue > this.tempVersion && intValue < intValue) {
                            this.tempVersion = intValue;
                            dinamicTemplate3.version = String.valueOf(this.tempVersion);
                        }
                        return true;
                    } catch (NumberFormatException unused) {
                        return false;
                    }
                }
            });
            if (list == null || list.length == 0) {
                return null;
            }
            dinamicTemplate3.name = str;
            return dinamicTemplate3;
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    public byte[] readLocalLayoutFileWithoutAccessDB(@NonNull String str) throws IOException {
        TemplateCache templateCache2 = getTemplateCache();
        byte[] bArr = templateCache2.memCache.get(str);
        if (bArr != null) {
            return bArr;
        }
        File file = new File(templateCache2.getRootDir(), str);
        if (!file.exists()) {
            return null;
        }
        byte[] readTemplateFromFile = templateCache2.readTemplateFromFile(file);
        templateCache2.memCache.put(str, readTemplateFromFile);
        return readTemplateFromFile;
    }

    @Nullable
    public byte[] readLocalLayoutFileAndUpdateDB(@NonNull String str) {
        TemplateCache templateCache2 = getTemplateCache();
        byte[] bArr = null;
        try {
            byte[] bArr2 = templateCache2.memCache.get(str);
            if (bArr2 != null) {
                return bArr2;
            }
            try {
                return templateCache2.fetchTemplateFromDisk(str, new TemplatePerfInfo());
            } catch (Throwable th) {
                th = th;
                bArr = bArr2;
                Log.e(TAG, "read local layout file exception", th);
                return bArr;
            }
        } catch (Throwable th2) {
            th = th2;
            Log.e(TAG, "read local layout file exception", th);
            return bArr;
        }
    }

    public byte[] readLocalLayoutFileFromAsset(String str, String str2) {
        byte[] bArr = null;
        try {
            byte[] bArr2 = getTemplateCache().memCache.get(str2);
            if (bArr2 != null) {
                return bArr2;
            }
            try {
                return readAssert(str, str2);
            } catch (Throwable th) {
                th = th;
                bArr = bArr2;
                Log.e(TAG, "read local layout file from asset exception", th);
                return bArr;
            }
        } catch (Throwable th2) {
            th = th2;
            Log.e(TAG, "read local layout file from asset exception", th);
            return bArr;
        }
    }

    public byte[] readAssert(String str, String str2) {
        String str3 = str + "/" + str2 + ".xml";
        try {
            return IOUtils.read(Dinamic.getContext().getAssets().open(str3));
        } catch (IOException e) {
            Log.e(TAG, "readAssert exception: " + str3, e);
            return null;
        }
    }

    public void setTemplateCacheSize(int i) {
        this.templateCache.updateMemCacheSize(i);
    }
}
