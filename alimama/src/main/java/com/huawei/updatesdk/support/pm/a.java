package com.huawei.updatesdk.support.pm;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import com.huawei.updatesdk.support.pm.c;
import java.io.File;

public class a {
    private static void a() {
        g.a(4, -10002);
    }

    protected static void a(Context context, b bVar) {
        b(context, bVar);
    }

    private static void b(Context context, b bVar) {
        if (bVar == null) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("InstallProcess", "system install failed,task is null");
            return;
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("InstallProcess", "systemInstall begin!!!task:" + bVar.toString());
        bVar.a(c.a.NOT_HANDLER);
        g.a(3, 1);
        File file = new File(bVar.f());
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("InstallProcess", "system install failed,file not existed filePath:" + bVar.f());
            g.a(4, -10003);
            return;
        }
        Intent intent = new Intent(context, PackageInstallerActivity.class);
        intent.putExtra("install_path", bVar.f());
        intent.putExtra("install_packagename", bVar.e());
        intent.putExtra("install_change_path_times", bVar.h());
        if (!(context instanceof Activity)) {
            intent.setFlags(402653184);
        }
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            a();
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("InstallProcess", " can not start install !", e);
        }
    }
}
