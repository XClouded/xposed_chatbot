package com.taobao.android.dinamic.tempate;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicDefaultApplication;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.log.DinamicLogThread;
import com.taobao.android.dinamic.tempate.SerialTaskManager;
import com.taobao.android.dinamic.tempate.manager.LayoutFileManager;
import com.taobao.android.dinamic.tempate.manager.TemplateCache;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DTemplateManager {
    private static final String SPLITTER = "_";
    private static final String TAG = "DTemplateManager";
    private LruCache<String, Boolean> assetCache = new LruCache<>(100);
    private String assetPath = "dinamic";
    /* access modifiers changed from: private */
    public CacheStrategy cacheStrategy = CacheStrategy.STRATEGY_DEFAULT;
    private Context context;
    private int interval = 3000;
    private LayoutFileManager layoutFileManager;
    /* access modifiers changed from: private */
    public String module;
    private LruCache<String, Integer> resCache = new LruCache<>(100);
    private SerialTaskManager taskManager = new SerialTaskManager();

    public enum CacheStrategy {
        STRATEGY_DEFAULT,
        STRATEGY_ALLOW_VERSION_DEGRADE
    }

    public DTemplateManager(String str) {
        this.module = str;
        this.context = Dinamic.getContext();
        if (this.context == null) {
            this.context = DinamicDefaultApplication.getApplication();
            Dinamic.setDinamicContext(this.context);
        }
        this.layoutFileManager = new LayoutFileManager(this.context, str);
        this.layoutFileManager.setHttpLoader(DRegisterCenter.shareCenter().getHttpLoader());
    }

    public void setTemplateCacheSize(int i) {
        this.layoutFileManager.setTemplateCacheSize(i);
    }

    public static DTemplateManager defaultTemplateManager() {
        return Dinamic.getModuleContainer("default").dTemplateManager;
    }

    public static DTemplateManager templateManagerWithModule(String str) {
        if (TextUtils.isEmpty(str)) {
            return Dinamic.getModuleContainer("default").dTemplateManager;
        }
        return Dinamic.getModuleContainer(str).dTemplateManager;
    }

    public XmlResourceParser getLayoutParser(DinamicTemplate dinamicTemplate) {
        if (this.context == null || dinamicTemplate == null || TextUtils.isEmpty(dinamicTemplate.name)) {
            return null;
        }
        try {
            int defaultLayoutId = getDefaultLayoutId(dinamicTemplate.name);
            if (defaultLayoutId > 0) {
                Log.d(TAG, "Res parser is applied: " + dinamicTemplate.name);
                return this.context.getResources().getLayout(defaultLayoutId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Get layout parser exception", e);
        }
        return null;
    }

    public byte[] readLocalTemplate(DinamicTemplate dinamicTemplate) throws IOException {
        String templateKey = getTemplateKey(dinamicTemplate);
        if (TextUtils.isEmpty(templateKey)) {
            return null;
        }
        return this.layoutFileManager.readLocalLayoutFileWithoutAccessDB(templateKey);
    }

    public byte[] readAssert(DinamicTemplate dinamicTemplate) {
        return this.layoutFileManager.readLocalLayoutFileFromAsset(this.assetPath, getTemplateKey(dinamicTemplate));
    }

    public void downloadTemplates(List<DinamicTemplate> list, final DinamicTemplateDownloaderCallback dinamicTemplateDownloaderCallback) {
        AnonymousClass1 r0 = new SerialTaskManager.LayoutFileLoadListener() {
            public void onFinished(DownloadResult downloadResult) {
                if (dinamicTemplateDownloaderCallback != null) {
                    dinamicTemplateDownloaderCallback.onDownloadFinish(downloadResult);
                } else if (Dinamic.isDebugable()) {
                    DinamicLog.w(DTemplateManager.TAG, "DinamicTemplateDownloaderCallback is null");
                }
            }
        };
        SerialTaskManager.DownLoadTask downLoadTask = new SerialTaskManager.DownLoadTask(this.layoutFileManager, this.interval);
        downLoadTask.listener = r0;
        downLoadTask.templates = list;
        downLoadTask.module = this.module;
        this.taskManager.execute(downLoadTask);
    }

    public DinamicTemplate fetchExactTemplate(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return null;
        }
        long nanoTime = System.nanoTime();
        if (dinamicTemplate.isPreset()) {
            DinamicTemplate presetTemplate = getPresetTemplate(dinamicTemplate);
            logFetchExactTemplate(dinamicTemplate, presetTemplate, System.nanoTime() - nanoTime);
            return presetTemplate;
        }
        if (this.cacheStrategy == CacheStrategy.STRATEGY_DEFAULT) {
            DinamicTemplate isLocalLayoutFileExists = isLocalLayoutFileExists(dinamicTemplate);
            if (isLocalLayoutFileExists != null) {
                logFetchExactTemplate(dinamicTemplate, isLocalLayoutFileExists, System.nanoTime() - nanoTime);
                return isLocalLayoutFileExists;
            }
        } else if (this.cacheStrategy == CacheStrategy.STRATEGY_ALLOW_VERSION_DEGRADE) {
            DinamicTemplate isLocalLayoutFileExists2 = isLocalLayoutFileExists(dinamicTemplate);
            if (isLocalLayoutFileExists2 != null) {
                logFetchExactTemplate(dinamicTemplate, isLocalLayoutFileExists2, System.nanoTime() - nanoTime);
                return isLocalLayoutFileExists2;
            }
            DinamicTemplate fetchDegradeFile = fetchDegradeFile(dinamicTemplate);
            if (fetchDegradeFile != null) {
                logFetchExactTemplate(dinamicTemplate, fetchDegradeFile, System.nanoTime() - nanoTime);
                return fetchDegradeFile;
            }
        }
        DinamicTemplate presetTemplate2 = getPresetTemplate(dinamicTemplate);
        logFetchExactTemplate(dinamicTemplate, presetTemplate2, System.nanoTime() - nanoTime);
        return presetTemplate2;
    }

    public DinamicTemplate isLocalLayoutFileExists(DinamicTemplate dinamicTemplate) {
        if (!this.layoutFileManager.isLocalLayoutFileExists(getTemplateKey(dinamicTemplate))) {
            return null;
        }
        DinamicTemplate dinamicTemplate2 = new DinamicTemplate();
        dinamicTemplate2.templateUrl = dinamicTemplate.templateUrl;
        dinamicTemplate2.name = dinamicTemplate.name;
        dinamicTemplate2.version = dinamicTemplate.version;
        return dinamicTemplate2;
    }

    public DinamicTemplate fetchDegradeFile(DinamicTemplate dinamicTemplate) {
        return this.layoutFileManager.fetchDegradeFile(dinamicTemplate);
    }

    public DinamicTemplate getPresetTemplate(DinamicTemplate dinamicTemplate) {
        if (getDefaultLayoutId(dinamicTemplate.name) > 0) {
            DinamicTemplate dinamicTemplate2 = new DinamicTemplate();
            dinamicTemplate2.name = dinamicTemplate.name;
            dinamicTemplate2.version = "";
            return dinamicTemplate2;
        } else if (!isAssetExists(dinamicTemplate.name)) {
            return null;
        } else {
            DinamicTemplate dinamicTemplate3 = new DinamicTemplate();
            dinamicTemplate3.name = dinamicTemplate.name;
            dinamicTemplate3.version = "";
            return dinamicTemplate3;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getDefaultLayoutId(java.lang.String r6) {
        /*
            r5 = this;
            android.content.Context r0 = r5.context
            r1 = -1
            if (r0 == 0) goto L_0x004c
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x000c
            goto L_0x004c
        L_0x000c:
            android.util.LruCache<java.lang.String, java.lang.Integer> r0 = r5.resCache
            java.lang.Object r0 = r0.get(r6)
            java.lang.Integer r0 = (java.lang.Integer) r0
            if (r0 != 0) goto L_0x003e
            android.content.Context r2 = r5.context     // Catch:{ Exception -> 0x0036 }
            android.content.res.Resources r2 = r2.getResources()     // Catch:{ Exception -> 0x0036 }
            java.lang.String r3 = "layout"
            android.content.Context r4 = r5.context     // Catch:{ Exception -> 0x0036 }
            java.lang.String r4 = r4.getPackageName()     // Catch:{ Exception -> 0x0036 }
            int r2 = r2.getIdentifier(r6, r3, r4)     // Catch:{ Exception -> 0x0036 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x0036 }
            android.util.LruCache<java.lang.String, java.lang.Integer> r0 = r5.resCache     // Catch:{ Exception -> 0x0033 }
            r0.put(r6, r2)     // Catch:{ Exception -> 0x0033 }
            r0 = r2
            goto L_0x003e
        L_0x0033:
            r6 = move-exception
            r0 = r2
            goto L_0x0037
        L_0x0036:
            r6 = move-exception
        L_0x0037:
            java.lang.String r2 = "DTemplateManager"
            java.lang.String r3 = "Get layout parser exception"
            android.util.Log.e(r2, r3, r6)
        L_0x003e:
            if (r0 == 0) goto L_0x004b
            int r6 = r0.intValue()
            if (r6 != 0) goto L_0x0047
            goto L_0x004b
        L_0x0047:
            int r1 = r0.intValue()
        L_0x004b:
            return r1
        L_0x004c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.DTemplateManager.getDefaultLayoutId(java.lang.String):int");
    }

    private boolean isAssetExists(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Boolean bool = this.assetCache.get(str);
        if (bool != null) {
            return bool.booleanValue();
        }
        String str2 = str + ".xml";
        try {
            InputStream open = Dinamic.getContext().getAssets().open(this.assetPath + "/" + str2);
            this.assetCache.put(str, Boolean.valueOf(open != null));
            if (open != null) {
                return true;
            }
            return false;
        } catch (IOException unused) {
            this.assetCache.put(str, false);
            return false;
        }
    }

    public void setPreSetTemplateAssetPath(String str) {
        this.assetPath = str;
    }

    public void setCacheStrategy(CacheStrategy cacheStrategy2) {
        this.cacheStrategy = cacheStrategy2;
    }

    public void registerHttpLoader(TemplateCache.HttpLoader httpLoader) {
        this.layoutFileManager.setHttpLoader(httpLoader);
    }

    public String getTemplateKey(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return null;
        }
        if (dinamicTemplate.isPreset()) {
            return dinamicTemplate.name;
        }
        return dinamicTemplate.name + "_" + dinamicTemplate.version;
    }

    public String getTemplateKey(String str, String str2) {
        return str + "_" + str2;
    }

    public String getTemplateCategory(DinamicTemplate dinamicTemplate, int i) {
        return dinamicTemplate.name + "_" + dinamicTemplate.version + "_" + i;
    }

    public String getTemplateCategory(String str, String str2, int i) {
        return str + "_" + str2 + "_" + i;
    }

    public LayoutFileManager getLayoutFileManager() {
        return this.layoutFileManager;
    }

    private void logFetchExactTemplate(DinamicTemplate dinamicTemplate, DinamicTemplate dinamicTemplate2, long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            final DinamicTemplate dinamicTemplate3 = dinamicTemplate;
            final DinamicTemplate dinamicTemplate4 = dinamicTemplate2;
            final long j2 = j;
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("fetch exact template=origin template=");
                        sb.append(dinamicTemplate3);
                        sb.append("exact template=");
                        sb.append(dinamicTemplate4);
                        sb.append("consuming=");
                        double d = (double) j2;
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String access$000 = DTemplateManager.this.module;
                    CacheStrategy access$100 = DTemplateManager.this.cacheStrategy;
                    DinamicTemplate dinamicTemplate = dinamicTemplate3;
                    DinamicTemplate dinamicTemplate2 = dinamicTemplate4;
                    double d2 = (double) j2;
                    Double.isNaN(d2);
                    performMonitor.trackFetchExactTemplate(access$000, access$100, dinamicTemplate, dinamicTemplate2, d2 / 1000000.0d);
                }
            });
        }
    }

    public void setDownloaderCallbackInterval(int i) {
        this.interval = i;
    }
}
