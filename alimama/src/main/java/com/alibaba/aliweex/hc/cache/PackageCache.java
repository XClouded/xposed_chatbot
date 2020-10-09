package com.alibaba.aliweex.hc.cache;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.AppResConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.DigestUtils;
import android.text.TextUtils;
import com.alibaba.aliweex.plugin.WorkFlow;
import com.taobao.pha.core.rescache.Package;
import com.taobao.tao.log.TLog;
import com.taobao.weex.common.WXThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PackageCache implements Handler.Callback {
    private static final int CACHE_PACKAGE = 1;
    private static final int CACHE_PAGE = 2;
    private static final String DISK_CACHE_MAP_KEY = "diskcache_keyname_map";
    private static final String PACKAGEAPP_NAME_DEFAULT = "gwxcache";
    static final String PACKAGE_SPLITER = "/\\*combo\\*/";
    private static PackageCache sInstance;
    private Handler cacheHandler = new WXThread("WeexCache", (Handler.Callback) this).getHandler();
    private boolean gettingPackageApp = false;
    /* access modifiers changed from: private */
    public boolean hasGotPackageApp = false;
    private HashMap<String, String> mDiskCacheMap = new HashMap<>();

    public void destroy() {
    }

    private PackageCache() {
    }

    public static PackageCache getInstance() {
        if (sInstance == null) {
            synchronized (PackageCache.class) {
                if (sInstance == null) {
                    sInstance = new PackageCache();
                }
            }
        }
        return sInstance;
    }

    public void cachePackages(ArrayList<Package.Item> arrayList) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        obtain.obj = arrayList;
        this.cacheHandler.sendMessage(obtain);
    }

    public void cachePage(String str, String str2, byte[] bArr) {
        Message obtain = Message.obtain();
        obtain.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString("key", str2);
        bundle.putByteArray("data", bArr);
        bundle.putString("url", str);
        obtain.setData(bundle);
        this.cacheHandler.sendMessage(obtain);
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                if (message.obj == null || !(message.obj instanceof ArrayList)) {
                    return false;
                }
                System.currentTimeMillis();
                ArrayList arrayList = (ArrayList) message.obj;
                WeexCacheMsgPanel.d("开始缓存模块到本地");
                if (AssembleManager.SHOW_LOG) {
                    TLog.logd(AssembleManager.TAG, "PackageCache cache package start");
                }
                WorkFlow.Work.make(arrayList).next(new WorkFlow.Action<Package.Item, Package.RemoteInfo>() {
                    public Package.RemoteInfo call(Package.Item item) {
                        Package.RemoteInfo remoteInfo = item.remoteInfo;
                        Iterator it = item.depInfos.iterator();
                        while (it.hasNext()) {
                            Package.Info info = (Package.Info) it.next();
                            if (!TextUtils.isEmpty(info.from)) {
                                if ("zcache".equals(info.from)) {
                                    WeexCacheMsgPanel.d(String.format("ZCache中的模块缓存到本地:%s", new Object[]{info.name}));
                                    PackageCache.this.putCache(info);
                                } else if ("avfs".equals(info.from)) {
                                    WeexCacheMsgPanel.d(String.format("Avfs中的模块缓存到内存:%s", new Object[]{info.name}));
                                    PackageCache.this.putPackageInfoToMemoryCache(info);
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(remoteInfo.comboJsData)) {
                            String[] split = remoteInfo.comboJsData.split(PackageCache.PACKAGE_SPLITER);
                            int i = 0;
                            while (split.length == remoteInfo.remoteInfoIndex.size() && i < split.length) {
                                Package.Info info2 = (Package.Info) item.depInfos.get(((Integer) remoteInfo.remoteInfoIndex.get(i)).intValue());
                                info2.code = split[i].trim();
                                info2.from = "network";
                                WeexCacheMsgPanel.d(String.format("异步请求模块缓存到本地:%s", new Object[]{info2.name}));
                                PackageCache.this.putCache(info2);
                                i++;
                            }
                        }
                        return item.remoteInfo;
                    }
                }).next(new WorkFlow.EndAction<Package.RemoteInfo>() {
                    public void end(Package.RemoteInfo remoteInfo) {
                        WeexCacheMsgPanel.d("缓存模块到本地结束");
                    }
                }).flow();
                if (AssembleManager.SHOW_LOG) {
                    TLog.logd(AssembleManager.TAG, "PackageCache cache package end");
                }
                CachePerf.getInstance().cacheRatioStatistic(arrayList);
                return true;
            case 2:
                Bundle data = message.getData();
                if (data != null) {
                    String string = data.getString("key");
                    byte[] byteArray = data.getByteArray("data");
                    String md5ToHex = DigestUtils.md5ToHex(byteArray);
                    String string2 = data.getString("url");
                    if (!TextUtils.isEmpty(string2) && TextUtils.equals(string, md5ToHex)) {
                        String md5ToHex2 = DigestUtils.md5ToHex(string2);
                        String contentFromDisk = getContentFromDisk(md5ToHex2);
                        if (!TextUtils.equals(contentFromDisk, string)) {
                            if (!TextUtils.isEmpty(contentFromDisk)) {
                                removeOneItemFromDisk(contentFromDisk);
                            }
                            if (!TextUtils.isEmpty(string)) {
                                putContentToDiskCache(md5ToHex2, string);
                            }
                            if (!TextUtils.isEmpty(string) && byteArray != null) {
                                putContentToDiskCache(string, byteArray);
                                break;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void putCache(Package.Info info) {
        com.taobao.pha.core.rescache.PackageCache.getInstance().putCache(info);
    }

    public void tryToPutZCachePackageIntoMemroyCache(boolean z) {
        if (this.gettingPackageApp || !this.hasGotPackageApp || z) {
            this.gettingPackageApp = true;
            WorkFlow.Work.make(PACKAGEAPP_NAME_DEFAULT).runOnNewThread().next(new WorkFlow.Action<String, AppResConfig>() {
                public AppResConfig call(String str) {
                    ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(str);
                    if (appInfo == null) {
                        return null;
                    }
                    String zipResAbsolutePath = ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfo, ZipAppConstants.APP_RES_NAME, false);
                    if (TextUtils.isEmpty(zipResAbsolutePath)) {
                        return null;
                    }
                    String readFile = ZipAppFileManager.getInstance().readFile(zipResAbsolutePath);
                    if (!TextUtils.isEmpty(readFile)) {
                        return ZipAppUtils.parseAppResConfig(readFile, true);
                    }
                    return null;
                }
            }).cancel(new WorkFlow.CancelAction<AppResConfig>() {
                /* access modifiers changed from: protected */
                public boolean cancel(AppResConfig appResConfig) {
                    return appResConfig == null;
                }
            }).next(new WorkFlow.Action<AppResConfig, Void>() {
                public Void call(AppResConfig appResConfig) {
                    if (appResConfig.mResfileMap == null) {
                        return null;
                    }
                    for (String str : appResConfig.mResfileMap.keySet()) {
                        AppResConfig.FileInfo fileInfo = appResConfig.mResfileMap.get(str);
                        if (!TextUtils.isEmpty(fileInfo.url) && !fileInfo.url.endsWith(".wvc")) {
                            String streamByUrl = ZipAppUtils.getStreamByUrl(fileInfo.url);
                            if (!TextUtils.isEmpty(streamByUrl)) {
                                Uri parse = Uri.parse(fileInfo.url);
                                List<String> pathSegments = parse.getPathSegments();
                                String uri = parse.buildUpon().clearQuery().scheme("").build().toString();
                                if (uri.startsWith(":")) {
                                    uri = uri.substring(1);
                                }
                                Package.Info info = new Package.Info();
                                info.path = uri;
                                info.code = streamByUrl;
                                if (pathSegments != null && pathSegments.size() > 3) {
                                    info.name = pathSegments.get(1);
                                    info.version = pathSegments.get(2);
                                }
                                PackageCache.this.putPackageInfoToMemoryCache(info);
                                boolean unused = PackageCache.this.hasGotPackageApp = true;
                            }
                        }
                    }
                    return null;
                }
            }).next(new WorkFlow.Action<Void, Void>() {
                public Void call(Void voidR) {
                    return null;
                }
            }).onCancel(new WorkFlow.Flow.CancelListener() {
                public void onCancel() {
                }
            }).flow();
            this.gettingPackageApp = false;
        }
    }

    /* access modifiers changed from: private */
    public void putPackageInfoToMemoryCache(Package.Info info) {
        com.taobao.pha.core.rescache.PackageCache.getInstance().putPackageInfoToMemoryCache(info);
    }

    /* access modifiers changed from: package-private */
    public Package.Info getPackageInfoFromMemCache(String str) {
        return com.taobao.pha.core.rescache.PackageCache.getInstance().getPackageInfoFromMemCache(str);
    }

    /* access modifiers changed from: package-private */
    public String getPackageInfoFromDisk(String str) {
        return getContentFromDisk(str);
    }

    public String getPageFromDisk(String str) {
        return getContentFromDisk(str);
    }

    private void putContentToDiskCache(String str, byte[] bArr) {
        com.taobao.pha.core.rescache.PackageCache.getInstance().putContentToDiskCache(str, bArr);
    }

    private void putContentToDiskCache(String str, String str2) {
        com.taobao.pha.core.rescache.PackageCache.getInstance().putContentToDiskCache(str, str2);
    }

    private String getContentFromDisk(String str) {
        return com.taobao.pha.core.rescache.PackageCache.getInstance().getContentFromDisk(str);
    }

    private void removeOneItemFromDisk(String str) {
        com.taobao.pha.core.rescache.PackageCache.getInstance().removeOneItemFromDisk(str);
    }
}
