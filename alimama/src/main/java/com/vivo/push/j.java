package com.vivo.push;

import android.text.TextUtils;
import com.vivo.push.LocalAliasTagsManager;
import com.vivo.push.model.SubscribeAppInfo;
import com.vivo.push.model.UnvarnishedMessage;
import com.vivo.push.util.p;
import java.util.ArrayList;
import java.util.List;

/* compiled from: LocalAliasTagsManager */
final class j implements Runnable {
    final /* synthetic */ UnvarnishedMessage a;
    final /* synthetic */ LocalAliasTagsManager.LocalMessageCallback b;
    final /* synthetic */ LocalAliasTagsManager c;

    j(LocalAliasTagsManager localAliasTagsManager, UnvarnishedMessage unvarnishedMessage, LocalAliasTagsManager.LocalMessageCallback localMessageCallback) {
        this.c = localAliasTagsManager;
        this.a = unvarnishedMessage;
        this.b = localMessageCallback;
    }

    public final void run() {
        int targetType = this.a.getTargetType();
        String tragetContent = this.a.getTragetContent();
        if (!TextUtils.isEmpty(tragetContent) && targetType != 0) {
            switch (targetType) {
                case 3:
                    SubscribeAppInfo subscribeAppInfo = this.c.mSubscribeAppAliasManager.getSubscribeAppInfo();
                    if (subscribeAppInfo == null || subscribeAppInfo.getTargetStatus() != 1 || !subscribeAppInfo.getName().equals(tragetContent)) {
                        p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, tragetContent);
                        p.a(LocalAliasTagsManager.TAG, tragetContent + " has ignored ; current Alias is " + subscribeAppInfo);
                        return;
                    }
                case 4:
                    List<String> subscribeTags = this.c.mSubscribeAppTagManager.getSubscribeTags();
                    if (subscribeTags == null || !subscribeTags.contains(tragetContent)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(tragetContent);
                        p.a().b(LocalAliasTagsManager.DEFAULT_LOCAL_REQUEST_ID, (ArrayList<String>) arrayList);
                        p.a(LocalAliasTagsManager.TAG, tragetContent + " has ignored ; current tags is " + subscribeTags);
                        return;
                    }
            }
        }
        this.c.mHandler.post(new k(this));
    }
}
