package com.vivo.push;

import com.vivo.push.model.SubscribeAppInfo;
import java.util.ArrayList;

/* compiled from: LocalAliasTagsManager */
final class g implements Runnable {
    final /* synthetic */ LocalAliasTagsManager a;

    g(LocalAliasTagsManager localAliasTagsManager) {
        this.a = localAliasTagsManager;
    }

    public final void run() {
        boolean z;
        SubscribeAppInfo retrySubscribeAppInfo = this.a.mSubscribeAppAliasManager.getRetrySubscribeAppInfo();
        if (retrySubscribeAppInfo != null) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (retrySubscribeAppInfo.getTargetStatus() == 1) {
                p.a().a(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, retrySubscribeAppInfo.getName());
            } else if (retrySubscribeAppInfo.getTargetStatus() == 2) {
                p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, retrySubscribeAppInfo.getName());
            }
            z = true;
        } else {
            z = false;
        }
        ArrayList<String> retrySubscribeAppInfo2 = this.a.mSubscribeAppTagManager.getRetrySubscribeAppInfo();
        if (retrySubscribeAppInfo2 != null && retrySubscribeAppInfo2.size() > 0) {
            if (!z) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                z = true;
            }
            p.a().a(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, retrySubscribeAppInfo2);
        }
        ArrayList<String> retryUnsubscribeAppInfo = this.a.mSubscribeAppTagManager.getRetryUnsubscribeAppInfo();
        if (retryUnsubscribeAppInfo != null && retryUnsubscribeAppInfo.size() > 0) {
            if (!z) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, retryUnsubscribeAppInfo);
        }
    }
}
