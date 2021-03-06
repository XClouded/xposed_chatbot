package com.vivo.push.c;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import com.vivo.push.b.c;
import com.vivo.push.b.e;
import com.vivo.push.b.z;
import com.vivo.push.cache.ClientConfigManagerImpl;
import com.vivo.push.model.b;
import com.vivo.push.util.a;
import com.vivo.push.util.p;
import com.vivo.push.util.r;
import com.vivo.push.util.s;
import com.vivo.push.v;
import com.vivo.push.y;
import java.util.List;

/* compiled from: SendCommandTask */
final class aj extends v {
    aj(y yVar) {
        super(yVar);
    }

    /* access modifiers changed from: protected */
    public final void a(y yVar) {
        if (this.a == null) {
            p.d("SendCommandTask", "SendCommandTask " + yVar + " ; mContext is Null");
        } else if (yVar == null) {
            p.d("SendCommandTask", "SendCommandTask pushCommand is Null");
        } else {
            b a = s.a(this.a);
            int b = yVar.b();
            if (b != 0) {
                if (b == 2009) {
                    p.a(ClientConfigManagerImpl.getInstance(this.a).isDebug());
                    if (p.a()) {
                        com.vivo.push.p.a().k();
                        a aVar = new a();
                        aVar.a(this.a, "com.vivo.push_preferences.hybridapptoken_v1");
                        aVar.a();
                        a aVar2 = new a();
                        aVar2.a(this.a, "com.vivo.push_preferences.appconfig_v1");
                        aVar2.a();
                        if (!com.vivo.push.p.a().e()) {
                            ClientConfigManagerImpl.getInstance(this.a).clearPush();
                        }
                    }
                } else if (b != 2011) {
                    switch (b) {
                        case 2002:
                        case 2003:
                        case 2004:
                        case 2005:
                            if (a == null || a.c()) {
                                com.vivo.push.p.a().a(((c) yVar).h(), 1005);
                                break;
                            } else {
                                c cVar = (c) yVar;
                                int a2 = r.a(cVar);
                                if (a2 != 0) {
                                    com.vivo.push.p.a().a(cVar.h(), a2);
                                    return;
                                }
                            }
                            break;
                    }
                } else {
                    p.a(ClientConfigManagerImpl.getInstance(this.a).isDebug(((z) yVar).d()));
                }
            } else if (com.vivo.push.p.a().e()) {
                Context context = this.a;
                Intent intent = new Intent();
                intent.setPackage(context.getPackageName());
                intent.setClassName(context.getPackageName(), "com.vivo.push.sdk.service.CommandService");
                List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
                if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                    p.d("ModuleUtil", "disableDeprecatedService is null");
                } else {
                    PackageManager packageManager = context.getPackageManager();
                    ComponentName componentName = new ComponentName(context, queryIntentServices.get(0).serviceInfo.name);
                    if (packageManager.getComponentEnabledSetting(componentName) != 2) {
                        packageManager.setComponentEnabledSetting(componentName, 2, 1);
                    }
                }
                Context context2 = this.a;
                Intent intent2 = new Intent();
                intent2.setPackage(context2.getPackageName());
                intent2.setClassName(context2.getPackageName(), "com.vivo.push.sdk.service.LinkProxyActivity");
                List<ResolveInfo> queryIntentActivities = context2.getPackageManager().queryIntentActivities(intent2, 128);
                if (queryIntentActivities == null || queryIntentActivities.size() <= 0) {
                    p.d("ModuleUtil", "disableDeprecatedActivity is null");
                } else {
                    PackageManager packageManager2 = context2.getPackageManager();
                    ComponentName componentName2 = new ComponentName(context2, queryIntentActivities.get(0).activityInfo.name);
                    if (packageManager2.getComponentEnabledSetting(componentName2) != 2) {
                        packageManager2.setComponentEnabledSetting(componentName2, 2, 1);
                    }
                }
            }
            if (a == null) {
                p.d("SendCommandTask", "SendCommandTask " + yVar + " ; pushPkgInfo is Null");
                return;
            }
            String a3 = a.a();
            if (a.c()) {
                com.vivo.push.p.a().a(((c) yVar).h(), 1004);
                yVar = new e();
                p.d("SendCommandTask", "SendCommandTask " + yVar + " ; pkgName is InBlackList ");
            }
            com.vivo.push.a.a.a(this.a, a3, yVar);
        }
    }
}
