package com.alimama.share.qq;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alimama.share.interfaces.SocialFunction;
import java.util.ArrayList;

public class QQ extends SocialFunction {
    private static final String JUMPACTIVITY = "com.tencent.mobileqq.activity.JumpActivity";
    private static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";
    private static final String PACKAGE_QZONE = "com.qzone";
    private static final String PACKAGE_WECHAT = "com.tencent.mm";
    private static final String QZONEACTIVITY = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";
    private Context context;

    public QQ(Context context2) {
        this.context = context2;
    }

    public void shareImage(Uri uri, Boolean bool, String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setFlags(268435456);
        intent.setType("image/*");
        intent.setComponent(getComponents(bool.booleanValue()));
        this.context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public void shareText(String str, boolean z, String str2) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.setFlags(268435456);
        intent.setComponent(getComponents(z));
        this.context.startActivity(intent);
    }

    public void shareImages(ArrayList<Uri> arrayList, Boolean bool, String str, String str2) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.addFlags(268435456);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        intent.putExtra("Kdescription", str2);
        intent.setType("image/*");
        intent.setComponent(getComponents(bool.booleanValue()));
        this.context.startActivity(intent);
    }

    private ComponentName getComponents(boolean z) {
        if (z) {
            return new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity");
        }
        return new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
    }
}
