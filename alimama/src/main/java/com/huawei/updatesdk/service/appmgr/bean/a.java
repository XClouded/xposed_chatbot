package com.huawei.updatesdk.service.appmgr.bean;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.huawei.updatesdk.sdk.a.d.c;
import com.huawei.updatesdk.support.b.d;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.extension.UCExtension;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class a extends com.huawei.updatesdk.a.a.a {
    public static final String APIMETHOD = "client.https.diffUpgrade";
    public static final int DEFAULT_UPGRADE_RESULT = 0;
    public static final int FULL_UPGRADE_RESULT = 1;
    public static final int INSTALL_CHECK_DEFAULT = 0;
    public static final int PRE_DOWNLOAD_CLOSE = 0;
    private static final String TAG = "UpgradeRequest";
    private static final int TYPE_NOT_PREINSTALL = 0;
    private static final int TYPE_PREINSTALL = 2;
    private static final int TYPE_PREINSTALL_REMOVABLE = 1;
    private String cmp_ = "1";
    private int installCheck_ = 0;
    private int isFullUpgrade_ = 0;
    private int isUpdateSdk_ = 1;
    private int isWlanIdle_ = 0;
    private C0017a json_;
    private String maxMem_;

    /* renamed from: com.huawei.updatesdk.service.appmgr.bean.a$a  reason: collision with other inner class name */
    public static class C0017a extends com.huawei.updatesdk.sdk.service.c.a.b {
        private List<b> params_;

        public void a(List<b> list) {
            this.params_ = list;
        }
    }

    public static class b extends com.huawei.updatesdk.sdk.service.c.a.b {
        private String fSha2_;
        private int isPre_;
        private String oldVersion_;
        private String package_;
        private String sSha2_;
        private int targetSdkVersion_;
        private int versionCode_;

        public b() {
        }

        public b(PackageInfo packageInfo) {
            this.package_ = packageInfo.packageName;
            this.versionCode_ = packageInfo.versionCode;
            this.oldVersion_ = packageInfo.versionName == null ? BuildConfig.buildJavascriptFrameworkVersion : packageInfo.versionName;
            this.targetSdkVersion_ = packageInfo.applicationInfo.targetSdkVersion;
            this.isPre_ = a.b(packageInfo);
            if (packageInfo.signatures != null) {
                this.sSha2_ = com.huawei.updatesdk.sdk.a.d.a.a.b(com.huawei.updatesdk.sdk.a.d.a.a(d.a(packageInfo.signatures[0].toCharsString())));
            }
            this.fSha2_ = c.a(packageInfo.applicationInfo.sourceDir, MessageDigestAlgorithms.SHA_256);
        }
    }

    public static a a(List<PackageInfo> list) {
        a aVar = new a();
        aVar.u(APIMETHOD);
        aVar.j(String.valueOf(com.huawei.updatesdk.sdk.a.d.b.a.b(com.huawei.updatesdk.sdk.service.a.a.a().b()) / 1024));
        aVar.v("1.2");
        aVar.c(0);
        C0017a aVar2 = new C0017a();
        aVar.a(aVar2);
        ArrayList arrayList = new ArrayList();
        aVar2.a(arrayList);
        for (PackageInfo bVar : list) {
            arrayList.add(new b(bVar));
        }
        return aVar;
    }

    private static boolean a(ApplicationInfo applicationInfo) {
        String str;
        StringBuilder sb;
        String str2;
        int i = applicationInfo.flags;
        Integer a = com.huawei.updatesdk.support.c.a.a();
        if (a != null && (i & a.intValue()) != 0) {
            return true;
        }
        Field b2 = com.huawei.updatesdk.support.c.a.b();
        if (b2 == null) {
            return false;
        }
        try {
            return (b2.getInt(applicationInfo) & UCExtension.EXTEND_INPUT_TYPE_IDCARD) != 0;
        } catch (IllegalAccessException e) {
            str2 = TAG;
            sb = new StringBuilder();
            sb.append("can not get hwflags");
            str = e.toString();
            sb.append(str);
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(str2, sb.toString());
            return false;
        } catch (IllegalArgumentException e2) {
            str2 = TAG;
            sb = new StringBuilder();
            sb.append("can not get hwflags");
            str = e2.toString();
            sb.append(str);
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(str2, sb.toString());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static int b(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & 1) == 0) {
            return 0;
        }
        return a(packageInfo.applicationInfo) ? 1 : 2;
    }

    public static a k(String str) {
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.packageName = str;
        packageInfo.versionName = "1.0";
        packageInfo.versionCode = 1;
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.targetSdkVersion = 19;
        packageInfo.applicationInfo = applicationInfo;
        ArrayList arrayList = new ArrayList();
        arrayList.add(packageInfo);
        a a = a((List<PackageInfo>) arrayList);
        a.b(1);
        return a;
    }

    public void a(int i) {
        this.installCheck_ = i;
    }

    public void a(C0017a aVar) {
        this.json_ = aVar;
    }

    public void b(int i) {
        this.isFullUpgrade_ = i;
    }

    public void c(int i) {
        this.isWlanIdle_ = i;
    }

    public void j(String str) {
        this.maxMem_ = str;
    }
}
