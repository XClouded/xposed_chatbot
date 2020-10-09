package com.alibaba.aliweex.hc.cache;

import alimama.com.unwrouter.UNWRouter;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import com.alibaba.aliweex.plugin.WorkFlow;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.pha.core.rescache.Package;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class AssembleManager {
    private static final String DEP_PREFIX = "// ";
    private static final String PLACEHOLDER = "try{eval('##{\\u02D2}##')}catch(e){fd()}";
    public static boolean SHOW_LOG = true;
    public static final String TAG = "Page_Cache";
    private static AssembleManager sInstance;

    interface DependencyParser {
        ArrayList<Package.Item> parse(String str);
    }

    interface IAssemble {
        String assemble(ArrayList<Package.Item> arrayList);
    }

    public interface IPageLoaderCallback {
        void onFailed();

        void onFinished(String str);
    }

    private AssembleManager() {
    }

    public static AssembleManager getInstance() {
        if (sInstance == null) {
            synchronized (AssembleManager.class) {
                if (sInstance == null) {
                    sInstance = new AssembleManager();
                }
            }
        }
        return sInstance;
    }

    public void processAssembleWithDep(String str, String str2, IPageLoaderCallback iPageLoaderCallback) {
        processAssemble(str, str2, new ComboParserWithDep(), new PackageAssemble(), iPageLoaderCallback);
    }

    public void processAssembleWithTemplate(String str, byte[] bArr, IPageLoaderCallback iPageLoaderCallback) {
        String str2 = new String(bArr);
        processAssemble(str, str2, new ComboParser(), new PageAssemble(str2), iPageLoaderCallback);
    }

    public void processAssemble(final String str, String str2, final DependencyParser dependencyParser, IAssemble iAssemble, final IPageLoaderCallback iPageLoaderCallback) {
        WeexCacheMsgPanel.d("开始缓存方案处理");
        final long currentTimeMillis = System.currentTimeMillis();
        final CachePerf instance = CachePerf.getInstance();
        instance.pageName = str;
        final IAssemble iAssemble2 = iAssemble;
        final IPageLoaderCallback iPageLoaderCallback2 = iPageLoaderCallback;
        WorkFlow.Work.make(str2.trim()).runOnNewThread().sub(new WorkFlow.Action<String, ArrayList<Package.Item>>() {
            public ArrayList<Package.Item> call(String str) {
                WXLogUtils.i(AssembleManager.TAG, "compose packages start");
                long currentTimeMillis = System.currentTimeMillis();
                ArrayList<Package.Item> parse = dependencyParser.parse(str);
                WXLogUtils.i(AssembleManager.TAG, "parse dependency packages to request end time:" + (System.currentTimeMillis() - currentTimeMillis));
                return parse;
            }
        }).cancel(new WorkFlow.CancelAction<ArrayList<Package.Item>>() {
            /* access modifiers changed from: protected */
            public boolean cancel(ArrayList<Package.Item> arrayList) {
                return arrayList == null;
            }
        }).next(new WorkFlow.Action<ArrayList<Package.Item>, ArrayList<Package.Item>>() {
            public ArrayList<Package.Item> call(ArrayList<Package.Item> arrayList) {
                long currentTimeMillis = System.currentTimeMillis();
                ArrayList<Package.Item> packages = PackageRepository.getInstance().getPackages(arrayList);
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                instance.requestAllPkgsTime = currentTimeMillis2;
                WeexCacheMsgPanel.d("查询模块存储结束");
                WXLogUtils.d(AssembleManager.TAG, "request packages end time:" + currentTimeMillis2);
                return packages;
            }
        }).next(new WorkFlow.EndAction<ArrayList<Package.Item>>() {
            public void end(ArrayList<Package.Item> arrayList) {
                long currentTimeMillis = System.currentTimeMillis();
                String assemble = iAssemble2.assemble(arrayList);
                WXLogUtils.i(AssembleManager.TAG, "assemble packages time:" + (System.currentTimeMillis() - currentTimeMillis));
                WXLogUtils.i(AssembleManager.TAG, "compose packages finished");
                CachePerf.getInstance().processCacheAllTime = System.currentTimeMillis() - currentTimeMillis;
                iPageLoaderCallback2.onFinished(assemble);
                PackageCache.getInstance().cachePackages(arrayList);
            }
        }).onError(new WorkFlow.Flow.ErrorListener() {
            public void onError(Throwable th) {
                WXLogUtils.e(AssembleManager.TAG, "page loader got error:" + th.toString());
                StringBuilder sb = new StringBuilder();
                for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
                    StackTraceElement[] stackTrace = cause.getStackTrace();
                    if (stackTrace != null && stackTrace.length > 0) {
                        sb.append("Caused By:\n");
                        sb.append(cause.getClass() + ": " + cause.getMessage() + "\n");
                        for (StackTraceElement stackTraceElement : stackTrace) {
                            sb.append("at " + stackTraceElement.getClassName() + '.' + stackTraceElement.getMethodName() + '(' + stackTraceElement.getFileName() + Operators.CONDITION_IF_MIDDLE + stackTraceElement.getLineNumber() + ')' + 10);
                        }
                    }
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(UNWRouter.PAGE_NAME, (Object) str);
                jSONObject.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, (Object) sb.toString());
                instance.commitFail(jSONObject.toJSONString(), CachePerf.FAIL_CODE_CACHE_PROCESS_ERROR, "cache process got error");
                iPageLoaderCallback.onFailed();
            }
        }).onCancel(new WorkFlow.Flow.CancelListener() {
            public void onCancel() {
                WXLogUtils.e(AssembleManager.TAG, "page loader canceled");
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(UNWRouter.PAGE_NAME, (Object) str);
                instance.commitFail(jSONObject.toJSONString(), CachePerf.FAIL_CODE_CACHE_PROCESS_CANCELED, "cache process canceled");
                iPageLoaderCallback.onFailed();
            }
        }).flow();
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0068 A[SYNTHETIC, Splitter:B:24:0x0068] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0073 A[SYNTHETIC, Splitter:B:29:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeTemplateToFileForDebug(java.lang.String r6) {
        /*
            r5 = this;
            com.alibaba.aliweex.AliWeex r0 = com.alibaba.aliweex.AliWeex.getInstance()
            android.app.Application r0 = r0.getApplication()
            boolean r1 = r5.isExternalStorageWritable()
            if (r1 == 0) goto L_0x007c
            if (r0 == 0) goto L_0x007c
            java.io.File r1 = new java.io.File
            java.io.File r0 = r0.getExternalCacheDir()
            java.lang.String r2 = "wx_cache_tpl"
            r1.<init>(r0, r2)
            boolean r0 = r1.exists()
            if (r0 != 0) goto L_0x002f
            boolean r0 = r1.mkdirs()
            if (r0 != 0) goto L_0x002f
            java.lang.String r0 = "Page_Cache"
            java.lang.String r2 = "Directory not created"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r2)
        L_0x002f:
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            long r3 = java.lang.System.currentTimeMillis()
            r2.append(r3)
            java.lang.String r3 = ""
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r1, r2)
            r1 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0062 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0062 }
            byte[] r6 = r6.getBytes()     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r2.write(r6)     // Catch:{ Exception -> 0x005d, all -> 0x005a }
            r2.close()     // Catch:{ IOException -> 0x006c }
            goto L_0x007c
        L_0x005a:
            r6 = move-exception
            r1 = r2
            goto L_0x0071
        L_0x005d:
            r6 = move-exception
            r1 = r2
            goto L_0x0063
        L_0x0060:
            r6 = move-exception
            goto L_0x0071
        L_0x0062:
            r6 = move-exception
        L_0x0063:
            r6.printStackTrace()     // Catch:{ all -> 0x0060 }
            if (r1 == 0) goto L_0x007c
            r1.close()     // Catch:{ IOException -> 0x006c }
            goto L_0x007c
        L_0x006c:
            r6 = move-exception
            r6.printStackTrace()
            goto L_0x007c
        L_0x0071:
            if (r1 == 0) goto L_0x007b
            r1.close()     // Catch:{ IOException -> 0x0077 }
            goto L_0x007b
        L_0x0077:
            r0 = move-exception
            r0.printStackTrace()
        L_0x007b:
            throw r6
        L_0x007c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.cache.AssembleManager.writeTemplateToFileForDebug(java.lang.String):void");
    }

    public boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private class ComboParser implements DependencyParser {
        private ComboParser() {
        }

        public ArrayList<Package.Item> parse(String str) {
            JSONObject parseObject;
            JSONArray jSONArray;
            String parseDependency = parseDependency(str);
            if (TextUtils.isEmpty(parseDependency) || (parseObject = JSON.parseObject(parseDependency)) == null || (jSONArray = parseObject.getJSONArray("packages")) == null || jSONArray.isEmpty()) {
                return null;
            }
            WeexCacheMsgPanel.d("构造Package Items");
            ArrayList<Package.Item> arrayList = new ArrayList<>();
            for (int i = 0; i < jSONArray.size(); i++) {
                Package.Item item = new Package.Item();
                String string = jSONArray.getString(i);
                int indexOf = string.indexOf("??");
                String substring = string.substring(0, indexOf);
                item.group = substring;
                String[] split = string.substring(indexOf + "??".length()).split(",");
                Vector vector = new Vector();
                for (String str2 : split) {
                    Package.Info info = new Package.Info();
                    String[] split2 = str2.split("/");
                    if (split2.length > 2) {
                        info.name = split2[0];
                        info.version = split2[1];
                    }
                    info.relpath = str2;
                    info.path = substring + str2;
                    if (TextUtils.isEmpty(info.name)) {
                        String[] split3 = Uri.parse(info.path).getPath().split("/");
                        if (split3.length == 5) {
                            info.name = split3[2];
                            info.version = split3[3];
                        }
                    }
                    vector.add(info);
                }
                item.depInfos = vector;
                arrayList.add(item);
            }
            return arrayList;
        }

        /* access modifiers changed from: protected */
        public String parseDependency(String str) {
            WeexCacheMsgPanel.d("从页面中解析模块依赖");
            int lastIndexOf = str.lastIndexOf("\n");
            if (lastIndexOf >= 0) {
                return str.substring(lastIndexOf).trim().substring(AssembleManager.DEP_PREFIX.length());
            }
            return null;
        }
    }

    private class ComboParserWithDep extends ComboParser {
        /* access modifiers changed from: protected */
        public String parseDependency(String str) {
            return str;
        }

        private ComboParserWithDep() {
            super();
        }
    }

    private class PageAssemble extends PackageAssemble {
        String template;

        PageAssemble(String str) {
            super();
            this.template = str;
        }

        public String assemble(ArrayList<Package.Item> arrayList) {
            int indexOf = this.template.indexOf(AssembleManager.PLACEHOLDER);
            StringBuilder assemblePackage = assemblePackage(new StringBuilder(this.template.substring(0, indexOf)), arrayList);
            assemblePackage.append(this.template.substring(indexOf + AssembleManager.PLACEHOLDER.length()));
            return assemblePackage.toString();
        }
    }

    private class PackageAssemble implements IAssemble {
        private PackageAssemble() {
        }

        public String assemble(ArrayList<Package.Item> arrayList) {
            WeexCacheMsgPanel.d("开始拼接模块");
            return assemblePackage(new StringBuilder(), arrayList).toString();
        }

        /* access modifiers changed from: package-private */
        public StringBuilder assemblePackage(StringBuilder sb, ArrayList<Package.Item> arrayList) {
            Iterator<Package.Item> it = arrayList.iterator();
            int i = 0;
            while (it.hasNext()) {
                Package.Item next = it.next();
                int size = i + next.cachedInfoIndex.size();
                Iterator it2 = next.cachedInfoIndex.iterator();
                while (it2.hasNext()) {
                    sb.append(";");
                    sb.append(((Package.Info) next.depInfos.get(((Integer) it2.next()).intValue())).code);
                    sb.append(";");
                }
                if (!TextUtils.isEmpty(next.remoteInfo.comboJsData)) {
                    sb.append(";");
                    sb.append(next.remoteInfo.comboJsData);
                    sb.append(";");
                }
                i = size + next.remoteInfo.remoteInfoIndex.size();
            }
            WXLogUtils.i(AssembleManager.TAG, "join request size:" + i);
            WeexCacheMsgPanel.d("拼接模块结束");
            return sb;
        }
    }
}
