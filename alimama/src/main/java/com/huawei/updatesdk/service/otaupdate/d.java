package com.huawei.updatesdk.service.otaupdate;

import com.huawei.updatesdk.sdk.service.c.a.c;
import com.huawei.updatesdk.sdk.service.download.bean.DownloadTask;
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo;
import com.huawei.updatesdk.service.appmgr.bean.b;
import com.huawei.updatesdk.service.deamon.download.DownloadService;
import com.huawei.updatesdk.service.deamon.download.SecurityDownloadTask;
import java.util.List;

public class d {
    /* access modifiers changed from: private */
    public static a a;

    private static class a implements com.huawei.updatesdk.sdk.service.c.a.a {
        private a() {
        }

        public void a(c cVar, com.huawei.updatesdk.sdk.service.c.a.d dVar) {
        }

        public void b(c cVar, com.huawei.updatesdk.sdk.service.c.a.d dVar) {
            if (dVar instanceof b) {
                b bVar = (b) dVar;
                if (dVar.c() == 0 && dVar.d() == 0) {
                    if ((bVar.list_ != null && bVar.list_.size() != 0) || (bVar.notRcmList_ != null && bVar.notRcmList_.size() != 0)) {
                        ApkUpgradeInfo a = d.b(bVar.list_);
                        if (d.a != null) {
                            d.a.a(a);
                        }
                    } else if (d.a != null) {
                        d.a.a(dVar.c());
                    }
                } else if (d.a != null) {
                    d.a.b(dVar.c());
                }
            } else if (d.a != null) {
                d.a.a(dVar.c());
            }
        }
    }

    public static void a() {
        com.huawei.updatesdk.service.b.a.b.a((com.huawei.updatesdk.a.a.a) com.huawei.updatesdk.service.appmgr.bean.a.k("com.huawei.appmarket"), (com.huawei.updatesdk.sdk.service.c.a.a) new a());
    }

    public static void a(ApkUpgradeInfo apkUpgradeInfo) {
        DownloadService e = com.huawei.updatesdk.service.deamon.download.d.b().e();
        if (e != null) {
            DownloadTask b = e.b(apkUpgradeInfo.getPackage_());
            if (b == null) {
                SecurityDownloadTask securityDownloadTask = new SecurityDownloadTask();
                securityDownloadTask.a(0);
                securityDownloadTask.d(apkUpgradeInfo.getDownurl_());
                securityDownloadTask.f(apkUpgradeInfo.getPackage_());
                securityDownloadTask.a((long) apkUpgradeInfo.getSize_());
                securityDownloadTask.c(apkUpgradeInfo.getSha256_());
                e.a((DownloadTask) securityDownloadTask);
                if (a != null) {
                    a.b(apkUpgradeInfo);
                }
            } else if (b.n() > 4) {
                e.b(b);
            }
        } else {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("MarketDownloadManager", "downloadService == NULL");
        }
    }

    public static void a(a aVar) {
        a = aVar;
    }

    public static void a(String str) {
        com.huawei.updatesdk.service.deamon.download.d b = com.huawei.updatesdk.service.deamon.download.d.b();
        if (b != null && b.e() != null) {
            b.e().a(str);
        }
    }

    /* access modifiers changed from: private */
    public static ApkUpgradeInfo b(List<ApkUpgradeInfo> list) {
        if (list == null) {
            return null;
        }
        for (ApkUpgradeInfo next : list) {
            if ("com.huawei.appmarket".equals(next.getPackage_())) {
                return next;
            }
        }
        return null;
    }
}
