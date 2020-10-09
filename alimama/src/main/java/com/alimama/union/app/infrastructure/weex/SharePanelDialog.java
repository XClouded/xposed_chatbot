package com.alimama.union.app.infrastructure.weex;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alimama.moon.R;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.ArrayList;
import java.util.List;

public class SharePanelDialog extends Dialog implements View.OnClickListener {
    public static final String WEEX_SHARE = "weex_share";
    private Context mContext;
    private List<Uri> mImageList;
    private String mText;

    public SharePanelDialog(@NonNull Context context, String str, List<Uri> list) {
        super(context);
        this.mContext = context;
        this.mText = str;
        this.mImageList = list;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().requestFeature(1);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.share_panel_layout, (ViewGroup) null);
        inflate.findViewById(R.id.ll_share_wechat).setOnClickListener(this);
        inflate.findViewById(R.id.ll_share_qq).setOnClickListener(this);
        inflate.findViewById(R.id.ll_share_sina).setOnClickListener(this);
        inflate.findViewById(R.id.share_dialog_blank).setOnClickListener(this);
        setCancelable(false);
        setContentView(inflate);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setLayout(-1, -1);
        inflate.findViewById(R.id.btn_close).setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_close) {
            dismiss();
            return;
        }
        if (view.getId() == R.id.ll_share_wechat) {
            if (!ShareUtils.openWechat(getContext())) {
                ToastUtil.showToast(getContext(), (int) R.string.wechat_not_installed);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(WEEX_SHARE, "wechat").build());
        } else if (view.getId() == R.id.ll_share_qq) {
            if (!ShareUtils.openQq(getContext())) {
                ToastUtil.showToast(getContext(), (int) R.string.qq_not_installed);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(WEEX_SHARE, "qq").build());
        } else if (view.getId() == R.id.ll_share_sina) {
            if (!ShareUtils.shareWeibo(this.mContext, this.mText, (ArrayList) this.mImageList)) {
                ToastUtil.showToast(getContext(), (int) R.string.weibo_not_installed);
            }
            UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(WEEX_SHARE, "weibo").build());
        }
        dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
