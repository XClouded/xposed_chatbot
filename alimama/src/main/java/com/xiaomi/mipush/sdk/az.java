package com.xiaomi.mipush.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.HashMap;

class az extends Handler {
    final /* synthetic */ ay a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    az(ay ayVar, Looper looper) {
        super(looper);
        this.a = ayVar;
    }

    public void dispatchMessage(Message message) {
        ay ayVar;
        bd bdVar;
        HashMap<String, String> a2;
        ay ayVar2;
        bd bdVar2;
        if (message.what == 19) {
            String str = (String) message.obj;
            int i = message.arg1;
            synchronized (ao.class) {
                if (ao.a(ay.a(this.a)).a(str)) {
                    if (ao.a(ay.a(this.a)).a(str) < 10) {
                        if (bd.DISABLE_PUSH.ordinal() == i && "syncing".equals(ao.a(ay.a(this.a)).a(bd.DISABLE_PUSH))) {
                            ayVar2 = this.a;
                            bdVar2 = bd.DISABLE_PUSH;
                        } else if (bd.ENABLE_PUSH.ordinal() != i || !"syncing".equals(ao.a(ay.a(this.a)).a(bd.ENABLE_PUSH))) {
                            if (bd.UPLOAD_HUAWEI_TOKEN.ordinal() == i && "syncing".equals(ao.a(ay.a(this.a)).a(bd.UPLOAD_HUAWEI_TOKEN))) {
                                ayVar = this.a;
                                bdVar = bd.UPLOAD_HUAWEI_TOKEN;
                                a2 = j.a(ay.a(this.a), f.ASSEMBLE_PUSH_HUAWEI);
                            } else if (bd.UPLOAD_FCM_TOKEN.ordinal() == i && "syncing".equals(ao.a(ay.a(this.a)).a(bd.UPLOAD_FCM_TOKEN))) {
                                ayVar = this.a;
                                bdVar = bd.UPLOAD_FCM_TOKEN;
                                a2 = j.a(ay.a(this.a), f.ASSEMBLE_PUSH_FCM);
                            } else if (bd.UPLOAD_COS_TOKEN.ordinal() != i || !"syncing".equals(ao.a(ay.a(this.a)).a(bd.UPLOAD_COS_TOKEN))) {
                                if (bd.UPLOAD_FTOS_TOKEN.ordinal() == i && "syncing".equals(ao.a(ay.a(this.a)).a(bd.UPLOAD_FTOS_TOKEN))) {
                                    ayVar = this.a;
                                    bdVar = bd.UPLOAD_FTOS_TOKEN;
                                    a2 = j.a(ay.a(this.a), f.ASSEMBLE_PUSH_FTOS);
                                }
                                ao.a(ay.a(this.a)).b(str);
                            } else {
                                ayVar = this.a;
                                bdVar = bd.UPLOAD_COS_TOKEN;
                                a2 = j.a(ay.a(this.a), f.ASSEMBLE_PUSH_COS);
                            }
                            ayVar.a(str, bdVar, false, a2);
                            ao.a(ay.a(this.a)).b(str);
                        } else {
                            ayVar2 = this.a;
                            bdVar2 = bd.ENABLE_PUSH;
                        }
                        ayVar2.a(str, bdVar2, true, (HashMap<String, String>) null);
                        ao.a(ay.a(this.a)).b(str);
                    } else {
                        ao.a(ay.a(this.a)).c(str);
                    }
                }
            }
        }
    }
}
