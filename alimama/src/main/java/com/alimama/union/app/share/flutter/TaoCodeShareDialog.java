package com.alimama.union.app.share.flutter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.share.SocialCamp;
import java.util.ArrayList;

public class TaoCodeShareDialog extends Dialog implements View.OnClickListener {
    private ShareChannelClickHandler channelHandler;
    private final String mText;

    public TaoCodeShareDialog(@NonNull Context context, String str) {
        super(context, R.style.common_dialog_style);
        SocialCamp.getInstance().init(context);
        this.mText = str;
        this.channelHandler = new ShareChannelClickHandler(context);
    }

    public static void show(Activity activity, String str) {
        if (!activity.isFinishing()) {
            new TaoCodeShareDialog(activity, str).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_share_tao_code);
        findViewById(R.id.share_wechat).setOnClickListener(this);
        findViewById(R.id.share_qq).setOnClickListener(this);
        findViewById(R.id.share_weibo).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        setCancelable(true);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_close) {
            switch (id) {
                case R.id.share_wechat:
                    UTHelper.ShareFlutterPage.clickWechatCodeShare();
                    this.channelHandler.shareToWechat();
                    break;
                case R.id.share_qq:
                    UTHelper.ShareFlutterPage.clickQqCodeShare();
                    this.channelHandler.shareToQQ();
                    break;
                case R.id.share_weibo:
                    UTHelper.ShareFlutterPage.clickWeiboCodeShare();
                    this.channelHandler.shareToWeibo(this.mText, (ArrayList<Uri>) null);
                    break;
            }
        }
        dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
