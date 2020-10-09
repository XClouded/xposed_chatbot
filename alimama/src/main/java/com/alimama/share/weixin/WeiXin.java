package com.alimama.share.weixin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alimama.share.interfaces.SocialFunction;
import java.util.ArrayList;

public class WeiXin extends SocialFunction {
    private static final String IMAUI = "com.tencent.mm.ui.tools.ShareImgUI";
    private static final String PKG = "com.tencent.mm";
    private static final String TIMELINEUI = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    private Context context;

    public WeiXin(Context context2) {
        this.context = context2;
    }

    public void shareImage(Uri uri, Boolean bool, String str, String str2) {
        Intent intent = new Intent();
        intent.setComponent(getComponents(bool.booleanValue()));
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.STREAM", uri);
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        intent.putExtra("Kdescription", str2);
        intent.setType("image/*");
        run(str, intent);
    }

    public void shareText(String str, boolean z, String str2) {
        Intent intent = new Intent();
        intent.setComponent(getComponents(z));
        intent.setAction("android.intent.action.SEND");
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.setType("text/plain");
        run(str2, intent);
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
        run(str, intent);
    }

    private void run(String str, Intent intent) {
        if (TextUtils.isEmpty(str)) {
            this.context.startActivity(Intent.createChooser(intent, "分享图片"));
        } else {
            this.context.startActivity(Intent.createChooser(intent, "title"));
        }
    }

    private ComponentName getComponents(boolean z) {
        if (z) {
            return new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        }
        return new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
    }
}
