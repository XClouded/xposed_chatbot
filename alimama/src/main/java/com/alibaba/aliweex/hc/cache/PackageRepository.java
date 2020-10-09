package com.alibaba.aliweex.hc.cache;

import android.net.Uri;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.text.TextUtils;
import anet.channel.util.ALog;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.plugin.WorkFlow;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ta.utdid2.device.UTDevice;
import com.taobao.pha.core.rescache.Package;
import com.taobao.tao.log.TLog;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.http.WXHttpUtil;
import java.util.ArrayList;
import java.util.Vector;

public class PackageRepository {
    private static final String REQUEST_DEP_PKGS_URL_BASE = "https://weexdep.tmall.com/wh/fragment/act/weexdep";
    private static final String REQUEST_DEP_PKGS_URL_BASE_LEGACYE = "https://pages.tmall.com/wh/fragment/act/weexdep";
    private static final String REQUEST_DEP_PKGS_URL_BASE_PRE = "https://pre-wormhole.tmall.com/wh/fragment/act/weexdep";
    private static PackageRepository sInstance;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();

    private PackageRepository() {
        if (WXEnvironment.isApkDebugable()) {
            ALog.setUseTlog(false);
        }
    }

    public static PackageRepository getInstance() {
        if (sInstance == null) {
            synchronized (PackageRepository.class) {
                if (sInstance == null) {
                    sInstance = new PackageRepository();
                }
            }
        }
        return sInstance;
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Package.Item> getPackages(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.size() == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.size(); i++) {
            Package.Item item = new Package.Item();
            Vector vector = new Vector();
            item.depInfos = vector;
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            item.group = jSONObject.getString("host");
            JSONArray jSONArray2 = jSONObject.getJSONArray("mods");
            int i2 = 0;
            while (jSONArray2 != null && i2 < jSONArray2.size()) {
                JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                String string = jSONObject2.getString("name");
                String string2 = jSONObject2.getString("version");
                String string3 = jSONObject2.getString("fileName");
                Package.Info info = new Package.Info();
                info.name = string;
                info.version = string2;
                info.relpath = info.name + "/" + info.version + "/" + string3;
                StringBuilder sb = new StringBuilder();
                sb.append(item.group);
                sb.append(info.relpath);
                info.path = sb.toString();
                vector.add(info);
                i2++;
            }
            arrayList.add(item);
        }
        return getPackages((ArrayList<Package.Item>) arrayList);
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Package.Item> getPackages(ArrayList<Package.Item> arrayList) {
        WeexCacheMsgPanel.d("开始查询模块存储");
        getExistPackages(arrayList);
        getRemotePackages(arrayList);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0046  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.taobao.pha.core.rescache.Package.Item> getExistPackages(java.util.ArrayList<com.taobao.pha.core.rescache.Package.Item> r5) {
        /*
            r4 = this;
            com.alibaba.aliweex.AliWeex r0 = com.alibaba.aliweex.AliWeex.getInstance()
            com.alibaba.aliweex.IConfigAdapter r0 = r0.getConfigAdapter()
            if (r0 == 0) goto L_0x0025
            java.lang.String r1 = "weexcache_cfg"
            java.lang.String r2 = "get_pkgs_parallel2"
            java.lang.String r3 = "false"
            java.lang.String r0 = r0.getConfig(r1, r2, r3)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x0025
            java.lang.String r1 = "true"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0025
            r0 = 1
            goto L_0x0026
        L_0x0025:
            r0 = 0
        L_0x0026:
            boolean r1 = com.alibaba.aliweex.hc.cache.AssembleManager.SHOW_LOG
            if (r1 == 0) goto L_0x0040
            java.lang.String r1 = "Page_Cache"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "PackageRepository getExistPackages useParallel:"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.taobao.tao.log.TLog.logd((java.lang.String) r1, (java.lang.String) r2)
        L_0x0040:
            if (r0 == 0) goto L_0x0046
            r4.getPackagesParallel(r5)
            goto L_0x0049
        L_0x0046:
            r4.getPackagesSequency(r5)
        L_0x0049:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.cache.PackageRepository.getExistPackages(java.util.ArrayList):java.util.ArrayList");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.taobao.pha.core.rescache.Package.Item> getRemotePackages(java.util.ArrayList<com.taobao.pha.core.rescache.Package.Item> r5) {
        /*
            r4 = this;
            com.alibaba.aliweex.AliWeex r0 = com.alibaba.aliweex.AliWeex.getInstance()
            com.alibaba.aliweex.IConfigAdapter r0 = r0.getConfigAdapter()
            if (r0 == 0) goto L_0x0025
            java.lang.String r1 = "weexcache_cfg"
            java.lang.String r2 = "get_remote_pkgs_parallel2"
            java.lang.String r3 = "false"
            java.lang.String r0 = r0.getConfig(r1, r2, r3)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x0025
            java.lang.String r1 = "true"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0025
            r0 = 1
            goto L_0x0026
        L_0x0025:
            r0 = 0
        L_0x0026:
            boolean r1 = com.alibaba.aliweex.hc.cache.AssembleManager.SHOW_LOG
            if (r1 == 0) goto L_0x0040
            java.lang.String r1 = "Page_Cache"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "PackageRepository getRemotePackages useParallel:"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.taobao.tao.log.TLog.logd((java.lang.String) r1, (java.lang.String) r2)
        L_0x0040:
            java.lang.String r1 = "开始异步请求模块"
            com.alibaba.aliweex.hc.cache.WeexCacheMsgPanel.d(r1)
            if (r0 == 0) goto L_0x004d
            java.util.ArrayList r5 = r4.requestRemoteDepPkgsParallel(r5)
            goto L_0x0051
        L_0x004d:
            java.util.ArrayList r5 = r4.requestRemoteDepPkgsSequency(r5)
        L_0x0051:
            java.lang.String r0 = "异步请求模块结束"
            com.alibaba.aliweex.hc.cache.WeexCacheMsgPanel.d(r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.cache.PackageRepository.getRemotePackages(java.util.ArrayList):java.util.ArrayList");
    }

    private void getPackagesParallel(ArrayList<Package.Item> arrayList) {
        WorkFlow.Work.make(arrayList).next(new WorkFlow.EndAction<Package.Item>() {
            public void end(final Package.Item item) {
                String str = item.group;
                final StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                stringBuffer.append("??");
                WorkFlow.Work.make().branch(new WorkFlow.BranchParallel<Package.Info, Void, Package.Info>(item.depInfos) {
                    public Package.Info branch(int i, Package.Info info) {
                        String str = info.path;
                        Package.Info access$000 = PackageRepository.this.tryToGetFromMemoryCache(str, info.getMD5CacheKey());
                        if (access$000 != null) {
                            synchronized (PackageRepository.this.mLock) {
                                item.depInfos.set(i, access$000);
                                item.cachedInfoIndex.add(Integer.valueOf(i));
                            }
                            return access$000;
                        }
                        Package.Info access$200 = PackageRepository.this.tryToGetFromZcache(str, info);
                        if (access$200 != null) {
                            item.cachedInfoIndex.add(Integer.valueOf(i));
                            return access$200;
                        }
                        PackageRepository.this.tryToGetFromAvfsCache(info);
                        if (!TextUtils.isEmpty(info.code)) {
                            item.cachedInfoIndex.add(Integer.valueOf(i));
                            return info;
                        }
                        WeexCacheMsgPanel.d(String.format("模块需网络请求:%s/%s", new Object[]{info.name, info.version}));
                        synchronized (PackageRepository.this.mLock) {
                            stringBuffer.append(info.relpath + ",");
                            item.remoteInfo.remoteInfoIndex.add(Integer.valueOf(i));
                        }
                        return null;
                    }
                }).next(new WorkFlow.EndAction<WorkFlow.ParallelMerge<Package.Info>>() {
                    public void end(WorkFlow.ParallelMerge<Package.Info> parallelMerge) {
                        String stringBuffer = stringBuffer.toString();
                        if (stringBuffer.endsWith(",")) {
                            stringBuffer = stringBuffer.substring(0, stringBuffer.length() - 1);
                        }
                        item.remoteInfo.depComboUrl = stringBuffer;
                    }
                }).flow();
            }
        }).flow();
    }

    private void getPackagesSequency(ArrayList<Package.Item> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            Package.Item item = arrayList.get(i);
            String str = item.group;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("??");
            for (int i2 = 0; i2 < item.depInfos.size(); i2++) {
                Package.Info info = (Package.Info) item.depInfos.get(i2);
                String str2 = info.path;
                Package.Info tryToGetFromMemoryCache = tryToGetFromMemoryCache(str2, info.getMD5CacheKey());
                if (tryToGetFromMemoryCache != null) {
                    item.depInfos.set(i2, tryToGetFromMemoryCache);
                    item.cachedInfoIndex.add(Integer.valueOf(i2));
                } else if (tryToGetFromZcache(str2, info) != null) {
                    item.cachedInfoIndex.add(Integer.valueOf(i2));
                } else {
                    tryToGetFromAvfsCache(info);
                    if (!TextUtils.isEmpty(info.code)) {
                        item.cachedInfoIndex.add(Integer.valueOf(i2));
                    } else {
                        WeexCacheMsgPanel.d(String.format("模块需网络请求:%s/%s", new Object[]{info.name, info.version}));
                        sb.append(info.relpath);
                        sb.append(",");
                        item.remoteInfo.remoteInfoIndex.add(Integer.valueOf(i2));
                    }
                }
            }
            if (item.remoteInfo.remoteInfoIndex.size() > 0) {
                String sb2 = sb.toString();
                if (sb2.endsWith(",")) {
                    sb2 = sb2.substring(0, sb2.length() - 1);
                }
                item.remoteInfo.depComboUrl = sb2;
            }
        }
    }

    /* access modifiers changed from: private */
    public Package.Info tryToGetFromMemoryCache(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        Package.Info packageInfoFromMemCache = PackageCache.getInstance().getPackageInfoFromMemCache(str2);
        if (packageInfoFromMemCache != null) {
            packageInfoFromMemCache.from = Config.TYPE_MEMORY;
            packageInfoFromMemCache.requestTime = System.currentTimeMillis() - currentTimeMillis;
            WeexCacheMsgPanel.d(String.format("模块命中内存存储:%s/%s", new Object[]{packageInfoFromMemCache.name, packageInfoFromMemCache.version}));
        }
        return packageInfoFromMemCache;
    }

    /* access modifiers changed from: private */
    public Package.Info tryToGetFromZcache(String str, Package.Info info) {
        long currentTimeMillis = System.currentTimeMillis();
        String streamByUrl = ZipAppUtils.getStreamByUrl(str);
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (TextUtils.isEmpty(streamByUrl)) {
            return null;
        }
        info.from = "zcache";
        info.requestTime = currentTimeMillis2;
        info.code = streamByUrl;
        info.bytes = streamByUrl.getBytes();
        CachePerf.getInstance().alarmRequestSuccess("zcache", currentTimeMillis2, str);
        WeexCacheMsgPanel.d(String.format("模块命中ZCache缓存:%s/%s", new Object[]{info.name, info.version}));
        return info;
    }

    public Package.Info tryToGetFromAvfsCache(Package.Info info) {
        long currentTimeMillis = System.currentTimeMillis();
        String packageInfoFromDisk = PackageCache.getInstance().getPackageInfoFromDisk(info.key);
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (!TextUtils.isEmpty(packageInfoFromDisk)) {
            info.code = packageInfoFromDisk;
            info.bytes = packageInfoFromDisk.getBytes();
            info.from = "avfs";
            info.requestTime = currentTimeMillis2;
            CachePerf.getInstance().alarmRequestSuccess("avfs", currentTimeMillis2, info.path);
            WeexCacheMsgPanel.d(String.format("模块命中本地存储:%s/%s", new Object[]{info.name, info.version}));
        }
        return info;
    }

    /* access modifiers changed from: private */
    public String requestRemotePackage(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        DegradableNetwork degradableNetwork = new DegradableNetwork(AliWeex.getInstance().getApplication());
        Response syncSend = degradableNetwork.syncSend(getRequest(str), (Object) null);
        int statusCode = syncSend.getStatusCode();
        if (statusCode != 200) {
            TLog.loge(AssembleManager.TAG, "PackageRepository requestRemotePackage fail, request from cdn again");
            syncSend = degradableNetwork.syncSend(getRequest(str2), (Object) null);
            statusCode = syncSend.getStatusCode();
        }
        if (statusCode == 200) {
            byte[] bytedata = syncSend.getBytedata();
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            TLog.logd(AssembleManager.TAG, "PackageRepository requestRemotePackage time:" + currentTimeMillis2);
            return new String(bytedata);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("path", (Object) str);
        jSONObject.put("statusCode", (Object) Integer.valueOf(statusCode));
        CachePerf.getInstance().commitFail(jSONObject.toJSONString(), CachePerf.FAIL_CODE_PKG_REQUEST_FAIL, "request remote package failed");
        TLog.loge(AssembleManager.TAG, "PackageRepository requestRemotePackage failed:" + str + ", status code:" + statusCode);
        StringBuilder sb = new StringBuilder();
        sb.append("异步请求模块失败:");
        sb.append(statusCode);
        WeexCacheMsgPanel.d(sb.toString());
        throw new RequestRemotePackageFailedException("request remote package failed");
    }

    private Request getRequest(String str) {
        RequestImpl requestImpl = new RequestImpl(str);
        if (AliWeex.getInstance().getApplication() != null) {
            requestImpl.addHeader("user-agent", WXHttpUtil.assembleUserAgent(AliWeex.getInstance().getApplication().getApplicationContext(), WXEnvironment.getConfig()));
        }
        if (!TextUtils.isEmpty(CachePerf.getInstance().pageName)) {
            Uri parse = Uri.parse(CachePerf.getInstance().pageName);
            String queryParameter = parse.getQueryParameter("wh_pid");
            Uri.Builder clearQuery = parse.buildUpon().clearQuery();
            if (!TextUtils.isEmpty(queryParameter)) {
                clearQuery.appendPath(queryParameter);
            }
            requestImpl.addHeader("Referer", clearQuery.toString());
        }
        if (AliWeex.getInstance().getApplication() != null) {
            requestImpl.addHeader("utdid", UTDevice.getUtdid(AliWeex.getInstance().getApplication()));
        }
        return requestImpl;
    }

    private ArrayList<Package.Item> requestRemoteDepPkgsSequency(ArrayList<Package.Item> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            Package.Item item = arrayList.get(i);
            String str = REQUEST_DEP_PKGS_URL_BASE;
            IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
            if (configAdapter != null) {
                str = configAdapter.getConfig("weexcache_cfg", "weex_dep_host", REQUEST_DEP_PKGS_URL_BASE);
            }
            if (!TextUtils.isEmpty(item.group) && "g-assets.daily.taobao.net".equals(Uri.parse(item.group).getHost())) {
                str = REQUEST_DEP_PKGS_URL_BASE_PRE;
            }
            if (item.remoteInfo.remoteInfoIndex.size() > 0) {
                String builder = Uri.parse(str).buildUpon().appendQueryParameter("wh_dep", item.remoteInfo.depComboUrl).toString();
                item.remoteInfo.comboJsData = requestRemotePackage(builder, item.remoteInfo.depComboUrl);
            }
        }
        return arrayList;
    }

    private ArrayList<Package.Item> requestRemoteDepPkgsParallel(ArrayList<Package.Item> arrayList) {
        WorkFlow.Work.make().branch(new WorkFlow.BranchParallel<Package.Item, Void, String>(arrayList) {
            public String branch(int i, Package.Item item) {
                if (item.remoteInfo.remoteInfoIndex.size() <= 0) {
                    return null;
                }
                String str = PackageRepository.REQUEST_DEP_PKGS_URL_BASE;
                IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
                if (configAdapter != null) {
                    str = configAdapter.getConfig("weexcache_cfg", "weex_dep_host", PackageRepository.REQUEST_DEP_PKGS_URL_BASE);
                }
                if (!TextUtils.isEmpty(item.group) && "g-assets.daily.taobao.net".equals(Uri.parse(item.group).getHost())) {
                    str = PackageRepository.REQUEST_DEP_PKGS_URL_BASE_PRE;
                }
                String access$300 = PackageRepository.this.requestRemotePackage(Uri.parse(str).buildUpon().appendQueryParameter("wh_dep", item.remoteInfo.depComboUrl).toString(), item.remoteInfo.depComboUrl);
                item.remoteInfo.comboJsData = access$300;
                return access$300;
            }
        }).next(new WorkFlow.EndAction<WorkFlow.ParallelMerge<String>>() {
            public void end(WorkFlow.ParallelMerge<String> parallelMerge) {
            }
        }).flow();
        return arrayList;
    }

    public static class RequestRemotePackageFailedException extends RuntimeException {
        public RequestRemotePackageFailedException(String str) {
            super(str);
        }
    }
}
